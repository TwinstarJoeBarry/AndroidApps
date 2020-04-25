package edu.ncc.nest.nestapp;
/**
 * Copyright (C) 2019 The LibreFoodPantry Developers.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ncc.nest.nestapp.UIFoods.BabyFood;
import edu.ncc.nest.nestapp.UIFoods.BakedGoods;
import edu.ncc.nest.nestapp.UIFoods.Beverages;
import edu.ncc.nest.nestapp.UIFoods.Condiments;
import edu.ncc.nest.nestapp.UIFoods.Dairy;
import edu.ncc.nest.nestapp.UIFoods.Deli;
import edu.ncc.nest.nestapp.UIFoods.FrozenFood;
import edu.ncc.nest.nestapp.UIFoods.Grains;
import edu.ncc.nest.nestapp.UIFoods.Meat;
import edu.ncc.nest.nestapp.UIFoods.Poultry;
import edu.ncc.nest.nestapp.UIFoods.Produce;
import edu.ncc.nest.nestapp.UIFoods.Seafood;
import edu.ncc.nest.nestapp.UIFoods.ShelfFoods;
import edu.ncc.nest.nestapp.UIFoods.VegProteins;

public class Inventory extends AppCompatActivity implements View.OnClickListener {

    private static final int[] btnIDs = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11, R.id.btn12,
            R.id.btn13, R.id.btn14};
    private Button[] btns = new Button[btnIDs.length];

    @Override
    /**
     * onCreate method -
     * loads the 'activity_inventory layout' which has only a TextView as a placeholder for now
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        for (int i = 0; i < btns.length; i++) {
            btns[i] = (Button) findViewById(btnIDs[i]);
            btns[i].setOnClickListener(this);
        }
    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method - goes to the home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn1:
                launchBabyFood();
                break;

            case R.id.btn2:
                launchBakedGoods();
                break;

            case R.id.btn3:
                launchBeverages();
                break;

            case R.id.btn4:
                launchCondiments();
                break;

            case R.id.btn5:
                launchDairy();
                break;

            case R.id.btn6:
                launchDeli();
                break;

            case R.id.btn7:
                launchFrozenFoods();
                break;

            case R.id.btn8:
                launchGrains();
                break;

            case R.id.btn9:
                launchMeat();
                break;

            case R.id.btn10:
                launchPoultry();
                break;

            case R.id.btn11:
                launchProduce();
                break;

            case R.id.btn12:
                launchSeafood();
                break;

            case R.id.btn13:
                launchShelfFoods();
                break;

            case R.id.btn14:

                launchVegProteins();
                break;
        }
    }

    /**
     * launchBabyFood - starts the BabyFood activity
     */
    public void launchBabyFood() {
        Intent intent = new Intent(this, BabyFood.class);
        startActivity(intent);
    }

    /**
     * launchBakedGoods - starts the BakedGoods activity
     */
    public void launchBakedGoods() {
        Intent intent = new Intent(this, BakedGoods.class);
        startActivity(intent);
    }

    /**
     * launchBakedBeverages - starts the Beverages activity
     */
    public void launchBeverages() {
        Intent intent = new Intent(this, Beverages.class);
        startActivity(intent);
    }

    /**
     * launchCondiments - starts the Condiments activity
     */
    public void launchCondiments() {
        Intent intent = new Intent(this, Condiments.class);
        startActivity(intent);
    }

    /**
     * launchDairy - starts the Diary activity
     */
    public void launchDairy() {
        Intent intent = new Intent(this, Dairy.class);
        startActivity(intent);
    }

    /**
     * launchDeli - starts the Deli activity
     */
    public void launchDeli() {
        Intent intent = new Intent(this, Deli.class);
        startActivity(intent);
    }

    /**
     * launchFrozenFood - starts the FrozenFoods activity
     */
    public void launchFrozenFoods() {
        Intent intent = new Intent(this, FrozenFood.class);
        startActivity(intent);
    }

    /**
     * launchGrains - starts the Grains activity
     */
    public void launchGrains() {
        Intent intent = new Intent(this, Grains.class);
        startActivity(intent);
    }

    /**
     * launchMeta - starts the Meat activity
     */
    public void launchMeat() {
        Intent intent = new Intent(this, Meat.class);
        startActivity(intent);
    }

    /**
     * launchPoultry - starts the Poultry activity
     */
    public void launchPoultry() {
        Intent intent = new Intent(this, Poultry.class);
        startActivity(intent);
    }

    /**
     * launchProduce - starts the Produce activity
     */
    public void launchProduce() {
        Intent intent = new Intent(this, Produce.class);
        startActivity(intent);
    }

    /**
     * launchSeafood - starts the Seafood activity
     */
    public void launchSeafood() {
        Intent intent = new Intent(this, Seafood.class);
        startActivity(intent);
    }

    /**
     * launchShelfFoods - starts the ShelfFoods activity
     */
    public void launchShelfFoods() {
        Intent intent = new Intent(this, ShelfFoods.class);
        startActivity(intent);
    }

    /**
     * launchVegProteins - starts the VegProteins activity
     */
    public void launchVegProteins() {
        Intent intent = new Intent(this, VegProteins.class);
        startActivity(intent);
    }
}
