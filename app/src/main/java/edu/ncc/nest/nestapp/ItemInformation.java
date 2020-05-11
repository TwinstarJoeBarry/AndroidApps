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

// still need to implement camera, API call for category, connecting to scanner layout

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
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
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class ItemInformation extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DatePickerDialog.OnDateSetListener {

    ArrayList<String> categories;
    ArrayList<String> items;
    public static final String TAG_NAME = "name";
    public static final String TAG_TEST = "Testing";
  //  ArrayList<HashMap<String, String>> locationList;


    TextView catDisplay, itemDisplay, expDisplay, resultDisplay;

    int expirationMonth, expirationDay, expirationYear;

    @Override
    /**
     * onCreate method --
     * sets up the activity for use. The categories list is currently loading 4 hardcoded categories,
     * but should eventually be changed to loading the categories from a database (Google API or
     * FoodKeeper?).
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // locationList = new ArrayList<>();
        catDisplay = (TextView)findViewById(R.id.cat_result);
        itemDisplay = (TextView)findViewById(R.id.item_result);
        expDisplay = (TextView)findViewById(R.id.exp_result);
        resultDisplay = (TextView)findViewById(R.id.result);

        expirationYear = -1; //for testing in calculateResult method

        categories = new ArrayList<String>();
        items = new ArrayList<String>();

        new GetCategories().execute();


    }

    /**
     * onCreateOptionsMenu method --
     * creates the main menu in toolbar
     *
     * @param menu
     * @return boolean
     */
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

    /**
     * showCatMenu method --
     * onClick for the Category button; loads and shows the category menu using the categories
     * ArrayList instance variable.
     *
     * @param v - the Category button
     */
    public void showCatMenu(View v){
        Log.d(TAG_TEST, "testing");
        PopupMenu catPop = new PopupMenu(this, v);

        catPop.setOnMenuItemClickListener(this);
        for(int i = 0; i < categories.size(); i++) //loop to load menu using ArrayList
            catPop.getMenu().add(i, Menu.FIRST, i, categories.get(i));
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

    @Override
    /**
     * onMenuItemClick menu --
     * will handle menu item clicks on the two popup menus (category and item). This section will
     * need to be rewritten depending on how the data comes in from the API call. Currently, there is
     * a switch statement to handle any category clicks, with a default case that handles every item
     * click in the same way (if the menu selection is not in the categories ArrayList, we know it
     * is actually an item).
     *
     * @param item -- the MenuItem selected
     */
    public boolean onMenuItemClick(MenuItem item) {
        Log.d("TESTING", item.getTitle().toString());
        String selection = item.getTitle().toString();
        switch(categories.indexOf(selection)){
            case 0:
                //bakery
                items.clear();
                items.add("Pie");
                items.add("Cookie");
                items.add("Cannoli");
                items.add("Cake");

                catDisplay.setText(selection);
                itemDisplay.setText("");
                return true;
            case 1:
                //dairy
                items.clear();
                items.add("Milk");
                items.add("Cheddar cheese");
                items.add("Cream cheese");

                catDisplay.setText(selection);
                itemDisplay.setText("");
                return true;
            case 2:
                //beverages
                items.clear();
                items.add("Pepsi");
                items.add("Coffee");
                items.add("Orange juice");
                items.add("Lemonade");
                items.add("Iced tea");

                catDisplay.setText(selection);
                itemDisplay.setText("");
                return true;
            case 3:
                //fruit
                items.clear();
                items.add("Orange");
                items.add("Apple");

                catDisplay.setText(selection);
                itemDisplay.setText("");
                return true;
            default:
                //item selection
                itemDisplay.setText(selection);
                //add code to store as instance variable (String itemName, or int productId?)
                //depends on implementation of calculate button
                return true;
        }
        //return false;
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
                Log.d("********", "Here!!");
                // connect a BufferedReader to the input stream at URL
                reader = new BufferedReader(new InputStreamReader(inputStream));
                // store the data in a string and display in the Logcat window
                result = reader.readLine();
                Log.d(TAG, "returned string HENRY: " + result);

            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }


            return null;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);

            if (result != null) {
                Log.d(TAG, "about to start the JSON parsing" + result);
                try {
                    Toast.makeText(getApplicationContext(),"Gathering the food categories please wait",Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = new JSONArray(result);

                    for(int i = 0; i < jsonArray.length(); i++){
                        //pulling an object out of the array based on the index number
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        //getting the name of the main name of the category
                        String name = jsonObj.getString("name");
                            categories.add(name);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d(TAG, "Couldn't get any data from the url");
            }

        }
    }




}
