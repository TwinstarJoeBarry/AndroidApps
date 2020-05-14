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

// still need to implement camera, API call for items in category, connecting to scanner layout

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.PopupMenu;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;


public class ItemInformation extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {

    // category list and current selected category id
    private ArrayList<String> categories;
    private int selectedCategoryId;

    // product item list and current selected item id
    private ArrayList<String> items;
    private int selectedItemId;

    // Views that are updated programmatically
    ProgressBar progressBar;
    TextView catDisplay, itemDisplay, expDisplay, resultDisplay;

    // used for expiration date calculation
    int expirationMonth, expirationDay, expirationYear;

    /**
     * onCreate method --
     * sets up the activity for use. The categories list is currently loading 4 hardcoded categories,
     * but should eventually be changed to loading the categories from a database (Google API or
     * FoodKeeper?).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressbar);
        catDisplay = findViewById(R.id.cat_result);
        itemDisplay = findViewById(R.id.item_result);
        expDisplay = findViewById(R.id.exp_result);
        resultDisplay = findViewById(R.id.result);

        expirationYear = -1; //for testing in calculateResult method

        // initialize category list and current selection
        categories = new ArrayList<>();
        selectedCategoryId = -1;

        // initialize item list and current selection
        items = new ArrayList<>();
        selectedItemId = -1;

        // load categories into list
        new GetCategories().execute();

    }

    /**
     * onCreateOptionsMenu method --
     * creates the main menu in toolbar
     *
     * @param menu the menu whose options are being created
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * onOptionsItemSelected method --
     * executes actions in response to menu item being selected
     * @param item the MenuItem that was selected
     * @return true if the item was handled, false otherwise
     */
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

    /**
     * showCatMenu method --
     * onClick for the Category button; loads and shows the category menu using the categories
     * ArrayList instance variable.
     *
     * @param v - the Category button
     */
    public void showCatMenu(View v){
        // create popup menu for category selection
        PopupMenu catPop = new PopupMenu(this, v);
        // attach method to take action when category is selected by the user
        catPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Get the id of the selected category from the chosen menu item
                int newSelectedId = item.getItemId();
                // if category selection has changed, a new item will need to be selected
                // as well so clear out the selected item and its display
                if (newSelectedId != selectedCategoryId) {
                    selectedItemId = -1;
                    itemDisplay.setText("");
                }
                // update selected category id and display
                selectedCategoryId = newSelectedId;
                catDisplay.setText(item.getTitle().toString());
                return true;
            }
        });
        // build the popup menu content by creating menuitems from the categories list
        for(int i = 0; i < categories.size(); i++)
            // Menu.NONE means this menuitem should not be part of any group
            // i + 1 is our desired unique Id for this menuitem (it equals the category id)
            // i is for the menuitem's display ordering position
            // categories.get(i) is the "category (subcategory)" description
            catPop.getMenu().add(Menu.NONE, i+1, i, categories.get(i));
        // show the popup menu
        catPop.show();
    }

    /**
     * showItemMenu method --
     * onClick for the Item button; loads and shows the item menu using the items ArrayList instance
     * variable. Item menu is empty before a user selects a category.
     *
     * @param v - the Item button
     */
    public void showItemMenu(View v){
        //show item menu
        PopupMenu itemPop = new PopupMenu(this, v);
        itemPop.setOnMenuItemClickListener(this);
        for(int i = 0; i < items.size(); i++) //loop to load menu using ArrayList
            itemPop.getMenu().add(i, Menu.FIRST, i, items.get(i));
        itemPop.show();
    }

    /**
     * onMenuItemClick menu --
     * will handle menu item clicks on the two popup menus (category and item). This section will
     * need to be rewritten depending on how the data comes in from the API call. Currently, there is
     * a switch statement to handle any category clicks, with a default case that handles every item
     * click in the same way (if the menu selection is not in the categories ArrayList, we know it
     * is actually an item).
     ******************************************************************************************
     * UPDATE: as of closing of issue 39, the category popup menu clicks are handled elsewhere.
     * This method still needs re-writing (or replacement) for handling item selection.
     ******************************************************************************************
     * @param item -- the MenuItem selected
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // handle item selected...
        // figure out the text to be displayed and display it
        // itemDisplay.setText(...);
        // store the id of the selected item
        // selectedItemId = ...
        // (suggestion: have itemId stored in menuItem similar to categories)
        // do something regarding calculate button here (? - depends on implementation)
        return true;
    }

    /**
     * showDatePickerDialog method --
     * onClick for the Date button; creates and shows a DatePickerDialog initialized to the current
     * date.
     *
     * @param v - the Date button
     */
    public void showDatePickerDialog(View v){
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    /**
     * onDateSet method --
     * Called whenever a user selects a date in the DatePickerDialog and hits okay. Note the
     * parameter descriptions below to interpret results. Stores month, day, and year, and updates
     * the label.
     *
     * @param datePicker - the DatePickerDialog
     * @param i - Year
     * @param i1 - Month (0 based; Jan. is 0, Feb. is 1, etc.)
     * @param i2 - Day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        i1++; //increment month to a 1-based number
        String expirationDate = i1 + "/" + i2 + "/" + i;
        expDisplay.setText(expirationDate);

        //store values for later use
        expirationMonth = i1;
        expirationDay = i2;
        expirationYear = i;
    }

    /**
     * calculateResult method --
     * onClick for the Calculate button; currently ensures an item and date have been selected,
     * and edits the Result label.
     *
     * @param v - the Calculate button
     */
    public void calculateResult(View v){
        //read from database using UPC code, or read from FoodKeeper and store to database if new item
        //calculate result using DOP of selected item and given expiration date, then set result label

        //placeholder code:
        if(expirationYear == -1 || itemDisplay.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Cannot calculate. Please select an item and " +
                    "date, then try again.", Toast.LENGTH_LONG).show();
        else
            resultDisplay.setText("Calculated result for " + itemDisplay.getText() + ", expiring " +
                    expirationMonth + "/" + expirationDay + "/" + expirationYear + " will go here.");
    }



    /**
     * inner class that will access the rest API and process the JSON returned
     *
     */
    private class GetCategories extends AsyncTask<Void, Void, Void> {
        private final String TAG = GetCategories.class.getSimpleName();
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "on pre execute");
            // give a little Toast message to explain any delay...
            Toast.makeText(getApplicationContext(),"Gathering the food categories please wait",
                    Toast.LENGTH_SHORT).show();
            // show the progress circle
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                String apiCall = "https://foodkeeper-api.herokuapp.com/categories";
                Log.d(TAG, "apiCall = " + apiCall);
                URL url = new URL(apiCall);
                // connect to the site to read information
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // store the data retrieved by the request
                InputStream inputStream = urlConnection.getInputStream();
                // no data returned by the request, so terminate the method
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                // connect a BufferedReader to the input stream at URL
                reader = new BufferedReader(new InputStreamReader(inputStream));
                // store the data in a string and display in the Logcat window
                result = reader.readLine();
                Log.d(TAG, "returned string: " + result);

            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);

            if (result != null) {
                Log.d(TAG, "about to start the JSON parsing" + result);
                try {
                    // Note: the categories are returned in category id order by the API.  This
                    // lets us just use the position in our categories arraylist to figure out
                    // the corresponding category id (position + 1).
                    // The result is expected to be an array of category entries so start with that
                    JSONArray jsonArray = new JSONArray(result);
                    // iterate through the entries in the array
                    for(int i = 0; i < jsonArray.length(); i++){
                        //pulling an object out of the array based on the index number
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        //getting the name of the main name of the category
                        String name = jsonObj.getString("name");
                        // if the subcategory is not null, get it and append it to the name
                        if (!jsonObj.isNull("subcategory"))
                            name += " (" + jsonObj.getString("subcategory") + ")";
                        // add the category to the list
                        categories.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Couldn't get any data from the url");
            }
            // hide the progress circle
            progressBar.setVisibility(View.INVISIBLE);
        }
    }




}
