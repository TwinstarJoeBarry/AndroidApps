package edu.ncc.nest.nestapp;
/**
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
