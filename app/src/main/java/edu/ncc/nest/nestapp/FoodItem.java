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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.List;



import java.text.DateFormat;

import edu.ncc.nest.nestapp.FragmentsCheckExpirationDate.CheckExpirationSelectItemFragment;

/**
 * @deprecated This Activity is being replaced by a Fragment. ({@link CheckExpirationSelectItemFragment
 * })
 */
@Deprecated
public class FoodItem extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{


    /**
     *
     *
     *
     */


    private static final String TAG = "Henry";
    public String upc;              //reference variable for the barcode number
    private String foodItemTest;    //reference variable for the name of the item that the user inputs

    public TextView itemName;
    public TextView barcodeNum;
    public TextView finalDate;
    public TextView pantryLife;

    private AlertDialog.Builder message;
    private InventoryInfoSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d(TAG,"we are in the on create method Hank");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemName = findViewById(R.id.textView_itemName);
        barcodeNum = findViewById(R.id.txtView_barcode);
        pantryLife = findViewById(R.id.textView_finalDate);

        Intent intent = getIntent();
        String str = intent.getStringExtra("barcode");
        String var = "";

        var = barcodeNum.getText().toString();
        barcodeNum.setText(var+str);
        upc = intent.getStringExtra("barcode");

        datasource = new InventoryInfoSource(this);
        datasource.open();
        message = new AlertDialog.Builder(FoodItem.this);

        //when activity starts, it will search the local database to see if there is a record
        //with the same upc number that was just scanned.
        //if not found, it's the first time this barcode has been scanned so now it's up to the
        //user to manually input the item and add it to the database
        //this will be initiate the process when the user clicks the search button
        List<InventoryEntry> itemList = datasource.findProducts(upc);
        if(!itemList.isEmpty()) {//if found, display a dialog message
            Log.d(TAG, "the dopLife is " + itemList.get(0).getPantryLife());
            message.setTitle("The max Shelf life for this item is..");
            message.setMessage(itemList.get(0).getPantryLife()+"\nItem name: "
                    +itemList.get(0).getItemName()).show();

        }
        
        // created merge conflict - not sure about where this should be 
        Button dateButton = (Button) findViewById(R.id.expiration_date_entry);
        dateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                //create DatePicker
                DialogFragment datePicker = new DatePickerFragment();
                //show the DatePicker
                datePicker.show(getSupportFragmentManager(), "date picker");

            }

        });
    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }



    /**
     * onOptionsItemSelected method --
     * Determines what happens based on which item in the app bar was selected
     * @param item - the item that was selected
     * @return super.onOptionsItemSelected(item), a command to start the method again
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
     * A method that is called when the Ok button on the Calendar is clicked,
     * formatting the selected date into a string that is stored in an intent
     * and sent to the FinalDate activity for calculation
     *
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){

        Calendar c = Calendar.getInstance(); //get the current calendar instance

        c.set(Calendar.YEAR, year); //store the Calendar's year in the year variable
        c.set(Calendar.MONTH, month); //store the Calendar's month in the month variable
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth); //store the Calendar's day of the month in dayOfMonth

        //Format the date into a string
        String currentDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime());


        Intent intent = new Intent(this, FinalDate.class);

        //put the selected date in the intent
        intent.putExtra("SELECTED_DATE", currentDateString);

        //start the new activity with the formatted date from calendar
        startActivity(intent);

    }

    /**
     * looUpItem --
     *  this is the OnClick method for the search button..
     *  It will check the local database to see if the upc we are looking for already exists, and if true,
     *  then app will display a dialog message with dopLife and itemName
     *  if false, upc does not exist in the database and a call to the foodKeeper api is made by the GetItem()
     * @param view
     */
    public void lookUpItem(View view) {
        EditText edit = findViewById(R.id.edit2_txt);
        foodItemTest = edit.getText().toString();

       List<InventoryEntry> productList = datasource.findProducts(upc); //first search LocalDatabase, if not found, then call FoodKeeperAPi
       if(!productList.isEmpty()){
          Log.d(TAG,  "the dopLife is " + productList.get(0).getPantryLife());
           message.setTitle("The max Shelf life for this item is..");
           message.setMessage(productList.get(0).getPantryLife()+"\nItem name: "
                   +productList.get(0).getItemName()).show();

       }else { //product list is empty, upc not found in local database, make call to foodkeeper Api
           new GetItem().execute(); //this is where the foodKeeper api call begins
           Toast.makeText(FoodItem.this, "Please wait a moment, for search results to load...", Toast.LENGTH_SHORT).show();
       }

    }

    /**
     * toLocalDatabase --
     * onClick method for the toDatabase button. This will take the user to the LocalDatabase activity
     * @param view
     */
    public void toLocalDatabase(View view){
        Intent intent = new Intent(this, LocalDatabase.class);
        startActivity(intent);
    }

    /**
     * searchItem --
     * method used to display a selection dialog message with possible entries that match the description
     * of the fooodItem that the user has just scanned. If a valid selection is selected and confirmed, then
     * that selection will be added to our local database to be used in future scans and to help grow the database.
     * the fields that are not shown in the UI, but are included with the selection, and are added to the database are
     * the productID, productName, date of pantry Life, and the barcode number.
     * @param choices
     */
    public void searchItem( ArrayList<InventoryEntry> choices){

        if(!choices.isEmpty()) {

            final String[] itemsArray = new String[choices.size()];
            final String[] productIds = new String[choices.size()];
            final String[] pantryLife = new String[choices.size()];

            //int index = 0;
           for(int i=0; i< itemsArray.length;i++)
          {      itemsArray[i] = choices.get(i).getItemName();
                 productIds[i] = choices.get(i).getItemId();
                 pantryLife[i] = choices.get(i).getPantryLife();
            }


            Log.d(TAG,"have passed the for loop");

            AlertDialog.Builder builder = new AlertDialog.Builder(FoodItem.this);

            //boolean array for selected item
            final boolean[] checkedArray = new boolean[itemsArray.length];

            //final List<String> theList = Arrays.asList(strArray);

            //set alert dialog title
            builder.setTitle("Choose selection that best describes this item..");

            builder.setSingleChoiceItems ( itemsArray, itemsArray.length, new DialogInterface.OnClickListener(){

                /**
                 * This method will be invoked when a button in the dialog is clicked.
                 *
                 * @param dialog the dialog that received the click
                 * @param which  the button that was clicked (ex.
                 *               {@link DialogInterface#BUTTON_POSITIVE}) or the position
                 */
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (int i = 0; i < checkedArray.length; i++) {
                        checkedArray[i] = false;
                    }
                    checkedArray[which] = true;
                }
            });

            //set positive/yes button clcik listner
            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for (int i = 0; i < checkedArray.length; i++) {
                       boolean checked = checkedArray[i];
                       if (checked) {
                           if(i!= checkedArray.length-1) { //condition true if the selection is anything other 'none of the above'
                               itemName.setText(itemsArray[i] + "\n");

                               Log.d(TAG, "item added to the database: "+ "productid:" + productIds[i]+" d_o_p" + pantryLife[i] + upc);
                               //the user has made a valid selection, so now a new record is added to the local database
                               //with the field values associated with that selection
                               datasource.addItem(productIds[i] ,itemsArray[i],pantryLife[i], upc);
                           }
                       }
                    }
                }
            });

            //set neutral/cancel button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do someting here
                }
            });

            AlertDialog dialog = builder.create();
            //show alert dialog
            dialog.show();
        }else{
            Log.d(TAG, "could not search item, because apparently options is null");
        }

    }

    /**
     * <p>Title: GetItem.java</p>
     *
     * <p>Description: class called by the lookUpItem(), when the user clicks
     * the search button. This class contains all the methods and code
     * needed to retrieve the data from the requested URL and parse the JSON to use the
     * information stored in it for application use and display.</p>
     */
    private class GetItem extends AsyncTask<Void, Void, Void> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "on pre execute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                //This API call will allow the program to retrieve all the data stored in the foodKeeper database, specifically
                //for all the products in the FoodKeeper. That data will then get parsed to be used in this app.
                URL url = new URL("https://foodkeeper-api.herokuapp.com/products");
                // connect to the site to read information
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // store the data retrieved by the request
                InputStream inputStream = urlConnection.getInputStream();

                // no data returned by the request, so terminate the method
                if (inputStream == null) {
                    // Nothing to do.
                    Log.d(TAG, "nothing to do");
                    return null;
                }

                // store the data into a BufferedReader so it can be stored into a string
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s = reader.readLine()) != null) {
                    result += s;
                }
            } catch (Exception e) {
                Log.i("HttpAsyncTask", "EXCEPTION: " + e.getMessage());
            }

            Log.d(TAG, "result =" + result);
            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
            //a string array of the words, of the foodItem that the user has entered
            String[] itemKeyWords = foodItemTest.split("\\s+");
            //arrayList the will hold the objects of the possible foodKeeper products that match
            //the description of the foodItem will are searching for
            ArrayList<InventoryEntry> choices = new ArrayList<InventoryEntry>();

            if (result != null) {
                Log.d(TAG, "about to start the JSON parsing" + result);
                try {
                    // code to parse the JSON here
                    JSONArray jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jObj = jsonArray.getJSONObject(i);

                        //a string of that JsonObject's keywords
                        String possibleKeyWords = jObj.getString("keywords").toLowerCase();

                        int j = 0;
                        //while loop should only run the length of the words of the productItemTest
                        while (j < itemKeyWords.length) {
                            //contains() is case sensitive, so both sides must be Lowercase
                            if (possibleKeyWords.contains(itemKeyWords[j].toLowerCase())) {

                                String strName="";
                                if(jObj.getString("name") != "null")
                                    strName = jObj.getString("name");

                                String strSubtitle="";
                                if(jObj.getString("subtitle") != "null")
                                    strSubtitle = jObj.getString("subtitle");

                                String fullName= strName+" "+strSubtitle;

                                String shelfLife = jObj.getJSONObject("dop_pantryLife").getString("max")+" "+
                                        jObj.getJSONObject("dop_pantryLife").getString("metric");

                                //adds this InventoryEntryList obj to the list
                                choices.add(new InventoryEntry( fullName, jObj.getString("id"), shelfLife));
                                break;
                            }
                            j++;
                        }

                    }

                    //after the iteration of the JsonArray, and none of the words provided by user matched any words in
                    //the keywords, that search item, might not exist in the foodKeeper api, or maybe a better search
                    //word/phrase would be useful
                    if (choices.size() == 0) {
                        Toast.makeText(FoodItem.this, "FoodItem could not be found in the FoodKeeperAPI", Toast.LENGTH_LONG).show();
                        //strArray = null;
                        Log.d(TAG, "choices size =0, for "+foodItemTest);
                    }
                    else{
                        Log.d(TAG, "for, "+foodItemTest+" .size= " + choices.size() + "\n");
                        choices.add(new InventoryEntry("None of the Above(hint* search possible categories of item)", "null", "null"));
                        searchItem(choices);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

        }
    }

}
