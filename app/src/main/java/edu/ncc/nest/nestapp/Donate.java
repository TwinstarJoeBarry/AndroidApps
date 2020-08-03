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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class Donate extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private String currentCategory;
    private String currentSubcategory;
    private String currentItem;
    private TextView theDate;
    private Button btnForCalender;

	/**
	 * onCreate method 
	 * sets up a Spinner that contains all the FoodKeeper categories
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //This block of code sets up the category spinner listing all the foodkeeper categories as options
        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Sets currentCategory to refer to an empty string object
        currentCategory = new String();
        //Sets currentSubcategory to refer to an empty string object
        currentSubcategory = new String();
        //Sets currentItem to refer to an empty string object
        currentItem = new String();
        //Sets the private variable theDate to refer to the TextView with the id expirationDate
        theDate = (TextView) findViewById(R.id.expirationDate);
        //Sets the private variable btnForCalendar to refer to the with the id selectDateBtn
        btnForCalender = (Button) findViewById(R.id.selectDateBtn);
        //Sets the onClick for btnForCalendar
        btnForCalender.setOnClickListener(this);
    }


    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }


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
        //If the id of the clicked view is the same as the id of selectDateBtn, then the showDatePickerDialog method is called
        if (v.getId() == R.id.selectDateBtn) {
            showDatePickerDialog();
        }
    }

    private void showDatePickerDialog(){
        //Instantiates a DatePickerDialog object and stores its reference in datePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        //Displays the date picker
        datePickerDialog.show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.categorySpinner) {
            if (position == 0) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display baby food items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.baby_food_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Baby Food";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 1) {
                //Sets the TextView subcategoryMessage to display the message "You must pick a subcategory."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("You must pick a subcategory.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner's adapter to display all the Baked Goods subcategories and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.baked_goods_subcategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(adapter);
                subCategorySpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Baked Goods";
            } else if (position == 2) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display beverage items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.beverages_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Beverages";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 3) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display condiments,sauces and canned good items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.condiments_sauces_and_canned_goods_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Condiments, Sauces & Canned Goods";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 4) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display dairy products and egg items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.dairy_products_and_eggs_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Dairy Products & Eggs";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 5) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display deli and prepared food items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.deli_and_prepared_foods_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Deli & Prepared Foods";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 6) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display food purchased frozen items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.food_purchased_frozen_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Food Purchased Frozen";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 7) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display grains,beans, and pasta items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.grains_beans_and_pasta_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Grains, Beans & Pasta";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else if (position == 8) {
                //Sets the TextView subcategoryMessage to display the message "You must pick a subcategory."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("You must pick a subcategory.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner's adapter to display all the Meat subcategories and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.meat_subcategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(adapter);
                subCategorySpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Meat";
            } else if (position == 9) {
                //Sets the TextView subcategoryMessage to display the message "You must pick a subcategory."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("You must pick a subcategory.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner's adapter to display all the Poultry subcategories and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.poultry_subcategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(adapter);
                subCategorySpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Poultry";
            } else if (position == 10) {
                //Sets the TextView subcategoryMessage to display the message "You must pick a subcategory."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("You must pick a subcategory.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner's adapter to display all the Produce subcategories and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.produce_subcategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(adapter);
                subCategorySpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Produce";
            } else if (position == 11) {
                //Sets the TextView subcategoryMessage to display the message "You must pick a subcategory."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("You must pick a subcategory.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner's adapter to display all the Seafood subcategories and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.seafood_subcategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCategorySpinner.setAdapter(adapter);
                subCategorySpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Seafood";
            } else if (position == 12) {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display shelf stable food items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.shelf_stable_foods_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Shelf Stable Foods";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            } else {
                //Sets the TextView subcategoryMessage to display the message "There is no subcategory. Select an item."
                ((TextView)findViewById(R.id.subcategoryMessage)).setText("There is no subcategory. Select an item.");
                //Retrieves and stores the reference to the subcategorySpinner in the variable subCategorySpinner
                Spinner subCategorySpinner = findViewById(R.id.subcategorySpinner);
                //Sets the subcategorySpinner to null
                subCategorySpinner.setAdapter(null);
                //Retrieves and stores the reference to the itemSpinner in the variable itemSpinner
                Spinner itemSpinner = findViewById(R.id.itemSpinner);
                //Sets the itemSpinner's adapter to display vegetarian protein items and sets its OnItemSelectedListener
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.vegetarian_protein_items, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                itemSpinner.setAdapter(adapter);
                itemSpinner.setOnItemSelectedListener(this);
                //Sets currentCategory to what is currently selected
                currentCategory = "Vegetarian Proteins";
                //Since this category has no subcategory, currentSubcategory is set to refer to an empty string
                currentSubcategory = "";
            }
        } else if (parent.getId() == R.id.subcategorySpinner) {
            if (currentCategory.equals("Baked Goods")) {
                if (position == 0) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.baked_goods_bakery_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Bakery";
                } else if (position == 1) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.baked_goods_baking_and_cooking_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Baking and Cooking";
                } else {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.baked_goods_refrigerated_dough_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Refrigerated Dough";
                }
            } else if (currentCategory.equals("Meat")) {
                if (position == 0) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.meat_fresh_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Fresh";
                } else if (position == 1) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.meat_shelf_stable_foods_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Shelf Stable Foods";
                } else if (position == 2) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.meat_smoked_or_processed_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Smoked or Processed";
                } else {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.meat_stuffed_or_assembled_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Stuffed or Assembled";
                }
            } else if (currentCategory.equals("Poultry")) {
                if (position == 0) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.poultry_cooked_or_processed_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Cooked or Processed";
                } else if (position == 1) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.poultry_fresh_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Fresh";

                } else if (position == 2) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.poultry_shelf_stable_foods_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Shelf Stable Foods";
                } else {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.poultry_stuffed_or_assembled_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Stuffed or Assembled";
                }
            } else if (currentCategory.equals("Produce")) {
                if (position == 0) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.produce_fresh_fruits_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Fresh Fruits";
                } else {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.produce_fresh_vegetables_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Fresh Vegetables";
                }
            } else {
                if (position == 0) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.seafood_fresh_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Fresh";
                } else if (position == 1) {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.seafood_shellfish_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Shellfish";
                } else {
                    Spinner itemSpinner = findViewById(R.id.itemSpinner);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                            R.array.seafood_smoked_items, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    itemSpinner.setAdapter(adapter);
                    itemSpinner.setOnItemSelectedListener(this);
                    currentSubcategory = "Smoked";
                }
            }
        } else {
            //Gets the item currently selected in the form of a String and sets currentItem to refer to it
            currentItem = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Stores the date in the format month/dayOfMonth/year in the variable date
        String date = " " + (month+1) + "/" + dayOfMonth + "/" + year;
        //Updates the TextView expirationDate to display the expiration date
        theDate.setText(date);
    }
}