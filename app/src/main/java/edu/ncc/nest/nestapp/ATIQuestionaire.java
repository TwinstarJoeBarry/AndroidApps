package edu.ncc.nest.nestapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import edu.ncc.nest.nestapp.AsyncTask.TaskExecutor;

/**
 * Activity hopefully to be used as an intent for addToInventory
 */
public class ATIQuestionaire extends AppCompatActivity implements View.OnClickListener {
    Button btn ;
    TextView categoryTitle;
    ArrayList<HashMap<String, String>> productList;
    final String PRODUCT_NAME = "products";
    final String SUB_PRODUCTS = "sub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_i_questionaire);
        btn = findViewById(R.id.atibutton);
        btn.setOnClickListener(this);

        //gets intent from addToInventory activity
        Intent intent = getIntent();
        categoryTitle = findViewById(R.id.categoryChosen);
        categoryTitle.setText(intent.getStringExtra("categoryLabel"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productList = new ArrayList<HashMap<String, String>>();
    }

    /**
     * implements the menu options for the toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * MenuItem selection
     * @param item Whatever is clicked on the menu item
     * @return Will return to the nest home once the
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * inner class that will access the rest API and process the JSON returned
     * used in order to get all the items from the API
     */
    private class Items extends TaskExecutor.BackgroundTask<Float, String> {

        @Override
        protected String doInBackground() {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // URL API call
                URL url = new URL("https://foodkeeper-api.herokuapp.com/products");

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
                return reader.readLine();

            } catch (Exception e) {
                Log.i("HttpAsyncTask", "EXCEPTION: " + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("TAG", "about to start the JSON parsing" + result);
            if (result != null) {
                try {
                    JSONArray jsonArr = new JSONArray(result);

                    //Used to get all the products from the API
                    for(int i = 0; i<jsonArr.length(); i++) {
                        JSONObject item = jsonArr.getJSONObject(i);
                        String categoryID = item.getString("categoryId");
                        int id = Integer.parseInt(categoryID);
                        String name = "";
                        String subCategories = "";
                        //Distinguishing when a different button from addToInventory class is pushed
                        // Category Id's are: Baby Food = 1; Baked Goods = 2,3,4;
                        //Beverages 5; Condiments, Sauces & Canned Goods = 6; Dairy Products & Eggs = 7
                        // Food Purchased Frozen = 8; Grains, Beans & Pasta = 9; Meat = 10,11,12,13; Poultry = 14,15,16,17
                        //Produce = 18,19; Seafood = 20,21,22; Shelf Stable Foods = 23; Vegetarian Proteins = 24; Deli & Prepared Foods = 25
                        switch(id){
                            case 1:
                                //Each one gets the name of the product and sub-poducts
                                //And put them in the HashMap, then adding them to the array of hash maps
                                if((categoryTitle.getText()).equals("Baby Food")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 2: case 3: case 4:
                                if((categoryTitle.getText()).equals("Baked Goods")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 5:
                                if((categoryTitle.getText()).equals("Beverages")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 6:
                                if((categoryTitle.getText()).equals("Condiments, Sauces & Canned Goods")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 7:
                                if((categoryTitle.getText()).equals("Dairy Products & Eggs")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 8:
                                if((categoryTitle.getText()).equals("Food Purchased Frozen")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 9:
                                if((categoryTitle.getText()).equals("Grains, Beans & Pasta")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 10: case 11: case 12: case 13:
                                if((categoryTitle.getText()).equals("Meat")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 14: case 15: case 16: case 17:
                                if((categoryTitle.getText()).equals("Poultry")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 18: case 19:
                                if((categoryTitle.getText()).equals("Produce")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 20: case 21: case 22:
                                if((categoryTitle.getText()).equals("Seafood")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 23:
                                if((categoryTitle.getText()).equals("Shelf Stable Foods")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 24:
                                if((categoryTitle.getText()).equals("Vegetarian Proteins")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                            case 25:
                                if((categoryTitle.getText()).equals("Deli & Prepared Foods")){
                                    name = item.getString("name");
                                    subCategories = item.getString("subtitle");
                                    HashMap<String, String> productNames = new HashMap<>();
                                    productNames.put(PRODUCT_NAME,name);
                                    productNames.put(SUB_PRODUCTS,subCategories);

                                    productList.add(productNames);
                                }
                                break;
                        }
                    }
                    // Adapter for the list view
                    ListAdapter adapter = new SimpleAdapter(
                            ATIQuestionaire.this, productList,
                            R.layout.list_ati,
                            new String[] { PRODUCT_NAME, SUB_PRODUCTS},
                            new int[] { R.id.productname, R.id.subproductname}
                    );

                    ListView myList = findViewById(R.id.listATI);
                    myList.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

        }

    }

    /**
     * home method - goes to the nest home screen
     */
    public void home() {
        TaskExecutor.executeAsRead(new Items());
    }

    /**
     * onClick - when the button is click the list view will appear
     * @param view - button clicked
     */
    @Override
        public void onClick(View view) {
        // this method should differentiate which button is clicked in the new activity
        // either for list view, ui#1, donate
        switch (view.getId()){
            case R.id.atibutton:
                TaskExecutor.executeAsRead(new Items());
                break;
            case R.id.uiLink:
                launchInterfaceOne();
                break;
            case R.id.donateLink:
                launchDonate();
                break;
        }

    }

    public void launchInterfaceOne() {
        Intent intent = new Intent(this, ItemInformation.class);
        startActivity(intent);
    }

    public void launchDonate() {
        Intent intent = new Intent(this, Donate.class);
        startActivity(intent);
    }
}
