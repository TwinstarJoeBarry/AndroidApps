package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FoodItem extends AppCompatActivity {

    public TextView itemName;
    public TextView barcodeNum;
    public TextView finalDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        itemName = findViewById(R.id.textView_itemName);
        barcodeNum = findViewById(R.id.txtView_barcode);
        finalDate = findViewById(R.id.textView_finalDate);

        Intent intent = getIntent();
        String str = intent.getStringExtra("barcode");
        String var="";
        var = itemName.getText().toString();
        itemName.setText(var+str);
        var = barcodeNum.getText().toString();
        barcodeNum.setText(var+str);
        var = finalDate.getText().toString();
        finalDate.setText(var+str);

    }
}
