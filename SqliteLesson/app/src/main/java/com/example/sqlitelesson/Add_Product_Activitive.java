package com.example.sqlitelesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_Product_Activitive extends AppCompatActivity {
    EditText edtName, edtPrice;
    Button btnOK, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_activitive);

        linkview();
        addEvent();
    }

    private void linkview() {
        edtName=findViewById(R.id.edtName);
        edtPrice=findViewById(R.id.edtPrice);
        btnCancel=findViewById(R.id.btnCancel);
        btnOK=findViewById(R.id.btnOK);
    }

    private void addEvent() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insert data to DB
                String name = edtName.getText().toString();
                double price = Double.parseDouble(edtPrice.getText().toString());

                ContentValues values = new ContentValues();
                values.put("ProductName", name);
                values.put("ProductPrice", price);

                long flag= MainActivity.database.insert(MainActivity.TB_NAME, null, values);

                if(flag > 0){
                    Toast.makeText(Add_Product_Activitive.this, "Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Add_Product_Activitive.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}