package com.example.sqlitelesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.models.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "product_db.db";
    public static final String DB_PATH_SUFFIX = "/databases/";
    public static final String TB_NAME="Product";
    public static SQLiteDatabase database = null;
    ListView lvProduct;
    ArrayAdapter adapter;

    Product selectedProduct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyDataBase();
        linkview();
//        loadData();
        registerForContextMenu(lvProduct);
        addEvent();



    }

    private void linkview() {
        lvProduct=findViewById(R.id.lvProduct);
    }

    private void loadData() {
        adapter =new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1);

        database = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

        //get all products
        //Cach 1
//        Cursor cursor = database.rawQuery("SELECT * FROM " + TB_NAME, null);
        //Cach 2
        Cursor cursor =database.query(TB_NAME,null,null,null,null,null,null);


        //get some products
//        Cursor cursor = database.rawQuery("SELECT * FROM " + TB_NAME + " WHERE ProductId=? Or ProductId=?",
//                new String[]{"1","3"});
//        Cursor cursor = database.query(TB_NAME, null,"ProductId=? Or ProductId=?",
//                new String[]{"2","1"}, null,null,null);
//doc du lieu
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name= cursor.getString(1);
            double price  = cursor.getDouble(2);

            Product p = new Product(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2));
            adapter.add(p);

        }

//dữ liệu giả
//        adapter.add(new Product(1, "Heniken", 19000));
//        adapter.add(new Product(2, "SaiGon", 19000));
//        adapter.add(new Product(3, "Larue", 19000));
        lvProduct.setAdapter(adapter);
    }

    private void copyDataBase(){
        try{
            File dbFile = getDatabasePath(DATABASE_NAME);
            if(!dbFile.exists()){
                if(CopyDBFromAsset()){
                    Toast.makeText(MainActivity.this,
                            "Copy database successful!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,
                            "Copy database fail!", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.e("Error: ", e.toString());
        }
    }

    private boolean CopyDBFromAsset() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH_SUFFIX +
                DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!f.exists()){
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024]; int length;
            while((length=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, length);
            }
            outputStream.flush(); outputStream.close(); inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }


    //---------------MENU------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnAdd){
            Intent intent = new Intent(MainActivity.this, Add_Product_Activitive.class);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mnEdit){
            //open edit activity
            Intent intent = new Intent(MainActivity.this, Edit_Product_Activity.class);
            startActivity(intent);

            //Attack data
            if(selectedProduct !=null){
                intent.putExtra("productInfo", selectedProduct);
                startActivity(intent);
            }
        }else if(item.getItemId() == R.id.mnDelete){
            //delete product
            if(selectedProduct!=null){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Conform delete product!");
                builder.setMessage("Are you sure want to delete this product: "+selectedProduct+"?");
                builder.setIcon(android.R.drawable.ic_delete);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int flag = database.delete(TB_NAME, "ProductId= ?", new String[] {selectedProduct.getProductID()+""});

                        if(flag > 0){
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            loadData();
                        }else {
                            Toast.makeText(MainActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.create().show();

            }


        }
        return super.onContextItemSelected(item);
    }

    private void addEvent() {
        lvProduct.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedProduct = (Product) adapter.getItem(i);
                return false;
            }
        });
    }

}