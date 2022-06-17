package com.example.sqlitelesson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.models.Product;

public class Edit_Product_Activity extends AppCompatActivity {

    EditText edtName, edtPrice;
    Button btnOk, btnCancel;
    Product p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        
        linkview();
        addEvent();
        getData();
    }

    private void linkview() {
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        btnOk = findViewById(R.id.btnOK);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void addEvent() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updateproduct ìno
                ContentValues values = new ContentValues();
                values.put("ProductName", edtName.getText().toString());
                values.put("ProductPrice", Double.parseDouble(edtPrice.getText().toString()));

                int flag = MainActivity.database.update(MainActivity.TB_NAME, values, "ProductId= ?",
                        new String[]{p.getProductID() + ""});

                if(flag > 0){
                    Toast.makeText(Edit_Product_Activity.this, "Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Edit_Product_Activity.this, "Fail!", Toast.LENGTH_SHORT).show();
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

    private void getData() {
        Intent intent = getIntent();
        p = (Product) intent.getSerializableExtra("productInfo");
        edtName.setText(p.getProductName());
        edtPrice.setText(String.valueOf(p.getProductPrice()));
    }
}