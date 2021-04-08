package edu.ncc.nest.nestapp;
/*
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
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.PopupMenu;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;


/**
 * @deprecated This Activity is being replaced by Fragments. ({@see edu.ncc.nest.nestapp.CheckExpirationDate})
 */
@Deprecated
public class ItemInformation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // category list and current selected category id
    private ArrayList<String> categories;
    private int selectedCategoryId;
    private boolean waitingForCategories;

    // product item list and current selected item id
    private ArrayList<Product> items;
    private int selectedItemPosition;
    private boolean waitingForItems;

    ProgressBar progressBar;
    TextView catDisplay, itemDisplay, expDisplay, resultDisplay, tipDisplay;
    EditText upcEntry;
    Button tipBut;

    // used for expiration date calculation
    int expirationMonth, expirationDay, expirationYear;
    String tip = "This tip text should to be populated from the selected Item (Product)";

    private NestDBDataSource dataSource;

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
        catDisplay = (TextView)findViewById(R.id.cat_result);
        itemDisplay = (TextView)findViewById(R.id.item_result);
        expDisplay = (TextView)findViewById(R.id.exp_result);
        resultDisplay = (TextView)findViewById(R.id.result);
        tipDisplay = (TextView)findViewById(R.id.tipDisplay);
        tipBut = (Button)findViewById(R.id.tips_button);
        upcEntry = findViewById(R.id.upc_entry);

        expirationYear = -1; //for testing in calculateResult method

        dataSource = new NestDBDataSource(this);

        // initialize category list and current selection
        categories = new ArrayList<>();
        selectedCategoryId = -1;

        // initialize item list and current selection
        items = new ArrayList<>();
        selectedItemPosition = -1;

        // load categories into list
        TaskHelper taskHelper = new TaskHelper(1);

        try {

            taskHelper.submit(new GetCategoriesTask()).get();

        } catch (ExecutionException | InterruptedException e) {

            e.printStackTrace();

        } finally {

            taskHelper.shutdown();

        }

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
        if (item.getItemId() == R.id.home_btn) {
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
        // nothing to show? skip it
        // todo show a message if waiting?
        if (waitingForCategories || waitingForItems || categories.isEmpty())
            return;
        // create popup menu for category selection
        PopupMenu catPop = new PopupMenu(this, v);
        // attach method to take action when category is selected by the user
        catPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Get the id of the selected category from the chosen menu item
                int newSelectedId = item.getItemId();
                // if category selection has changed, a new item will need to be selected
                // as well so clear out the selected item, its display and the items list
                // and fire off the AsyncTask to get the items list for the newly selected category
                if (newSelectedId != selectedCategoryId) {
                    selectedCategoryId = newSelectedId;
                    selectedItemPosition = -1;
                    itemDisplay.setText("");
                    items.clear();

                    TaskHelper taskHelper = new TaskHelper(1);

                    try {

                        taskHelper.submit(new GetItemsForCategoryTask()).get();

                    } catch (ExecutionException | InterruptedException e) {

                        e.printStackTrace();

                    } finally {

                        taskHelper.shutdown();

                    }

                }
                // update selected category id and display
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
     * launchScanner - starts the Scanner activity
     */
    public void launchScanner(View view) {
        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }

    /**
     * showItemMenu method --
     * onClick for the Item button; loads and shows the item menu using the items ArrayList instance
     * variable. Item menu is empty before a user selects a category.
     *
     * @param v - the Item button
     */
    public void showItemMenu(View v){
        // nothing to show? skip it
        // todo show a message if waiting?
        if (waitingForItems || items.isEmpty())
            return;
        // create popup menu for item selection
        PopupMenu itemPop = new PopupMenu(this, v);
        // attach method to take action when item is selected by the user
        itemPop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // update selected item position and display
                selectedItemPosition = item.getItemId();
                itemDisplay.setText(item.getTitle().toString());
                // todo do something regarding calculate button here (? - depends on implementation)
                // todo make use of ShelfLife (may need to have user choose) of selected `Product` to calc date
                return true;
            }
        });
        // build the popup menu content by creating menuitems from the items list
        for(int i = 0; i < items.size(); i++)
            // Menu.NONE means this menuitem should not be part of any group
            // i is our desired "Id" (position in the list, actually) for this menuitem
            // i is also for the menuitem's display ordering position
            // items.get(i). is the "item (subitem)" description
            itemPop.getMenu().add(Menu.NONE, i, i, items.get(i).getName());
            // todo should add subtitle if applicable, but this will necessitate using something other than PopupMenu
        // show the popup menu
        itemPop.show();
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
     * and edits the Result label. Also displays the tip button if necessary
     *
     * @param v - the Calculate button
     */
    public void calculateResult(View v){
        //read from database using UPC code, or read from FoodKeeper and store to database if new item
        //calculate result using DOP of selected item and given expiration date, then set result label

        //ensures tip button and display is not visible
        tipBut.setVisibility(Button.INVISIBLE);
        tipBut.setClickable(false);
        tipDisplay.setText(R.string.empty_string);
        tipDisplay.setVisibility(TextView.INVISIBLE);

        //placeholder code
        if(expirationYear == -1 || itemDisplay.getText().toString().equals(""))
        if(expirationYear == -1 || itemDisplay.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Cannot calculate. Please select an item and " +
                    "date, then try again.", Toast.LENGTH_LONG).show();
        else
            resultDisplay.setText("Calculated result for " + itemDisplay.getText() + ", expiring " +
                    expirationMonth + "/" + expirationDay + "/" + expirationYear + " will go here.");

        //sets the button and display to visible when there is a tip to display
        if(tip!=null)
        {
            tipDisplay.setVisibility(TextView.VISIBLE);
            tipBut.setVisibility(Button.VISIBLE);
            tipBut.setClickable(true);
        }
    }

    /**
     * tipFound method --
     * onClick for the tip button; sets the text of the text display.
     * @param v - the tip button
     */
    public void tipFound(View v){
        tipDisplay.setText("Tip(s):"+tip);
    }

    /**
     * Inner class to retrieve all categories from the FoodKeeper API
     */
    private class GetCategoriesTask extends BackgroundTask<Void, String> {
        private final String TAG = GetCategoriesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "on pre execute");
            // give a little Toast message to explain any delay...
            Toast.makeText(getApplicationContext(),"Gathering the food categories please wait",
                    Toast.LENGTH_SHORT).show();
            // show the progress circle
            progressBar.setVisibility(View.VISIBLE);
            waitingForCategories = true;

        }

        @Override
        protected String doInBackground() {

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
                String result = reader.readLine();
                Log.d(TAG, "returned string: " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

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
            waitingForCategories = false;

        }
    }

    /**
     * Inner class to retrieve all items for the currently selected category from the FoodKeeper API
     */
    private class GetItemsForCategoryTask extends BackgroundTask<Void, String> {
        private final String TAG = GetItemsForCategoryTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "on pre execute");
            // give a little Toast message to explain any delay...
            Toast.makeText(getApplicationContext(),"Gathering items for selected category, please wait",
                    Toast.LENGTH_SHORT).show();
            // show the progress circle
            progressBar.setVisibility(View.VISIBLE);
            waitingForItems = true;

        }

        @Override
        protected String doInBackground() {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                String apiCall = "https://foodkeeper-api.herokuapp.com/products/category/"
                        + selectedCategoryId;
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
                String result = reader.readLine();
                Log.d(TAG, "returned string: " + result);

                return result;

            } catch (Exception e) {
                Log.d(TAG, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d(TAG, "about to start the JSON parsing" + result);
                try {
                    // The result is expected to be an array of product entries so start with that
                    JSONArray jsonArray = new JSONArray(result);
                    // iterate through the entries in the array
                    for(int i = 0; i < jsonArray.length(); i++){
                        // add the product to the list
                        items.add(new Product(jsonArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Couldn't get any data from the url");
            }
            // hide the progress circle
            progressBar.setVisibility(View.INVISIBLE);
            waitingForItems = false;

        }
    }

    /**
     * processUPC method --
     * onClick for the Lookup button, processes the UPC code that was
     * entered or scanned-in as follows:
     * - lookup the UPC code in the NestUPCs table (might be in separate database at some point)
     * - if found, fill out all the remaining fields based on the associated foodkeeper product,
     *   including doing the calculating of the "real" expiration date(s) (would be good to
     *   do entry/select of "package" expiration date prior to UPC scan/enter)
     *  - if not found, start the NewNestUPC activity so the Nest volunteer can add it
     *   as a new upc in the database.  If that activity is successful it should return with
     *   the newly associated FoodKeeper product id.  Proceed then as if the UPC had been found
     *   to begin with.
     * @param view - the processUPC button
     */
    public void lookupUPC(View view){
        String upc = upcEntry.getText().toString();
        int productId = dataSource.getProductIdFromUPC(upc); // returns -1 if not found
        if (productId < 0) {
            // upc not found in Nest UPCs table, let's add it...
            Intent intent = new Intent(this, NewNestUPC.class);
            intent.putExtra("upc", upc);
            startActivityForResult(intent, 0);
        } else {
            processDataFromUPC();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result from NewNestUPC activity launched in processUPC method
        if (resultCode == RESULT_OK) {
            processDataFromUPC();
        }
    }

    /**
     * processDataFromUPC method --
     * this method looks up the upc, product, category and shelfLives info for the given
     * upc code and does all the screen fields filling and calculating of expiration(s) and
     * displaying of the info
     */
    private void processDataFromUPC() {
        String upc = upcEntry.getText().toString();
        // we get the relevant fields from the Nest UPCs, product and category tables all in one object
        NestUPC upcData = dataSource.getNestUPC(upc);
        if (upcData == null)
            // todo give message ?
            return;

        // use the datasource to get all applicable shelf life information for the product
        List<ShelfLife> shelfLives = dataSource.getShelfLivesForProduct(upcData.getProductId());

        // todo still need to populate appropriate fields and parse the shelfLives to calculatre
        //  expiration date(s) and display results/info appropriately

    }


}
