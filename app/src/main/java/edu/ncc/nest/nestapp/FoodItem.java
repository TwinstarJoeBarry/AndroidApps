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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodItem extends AppCompatActivity {

    private static final String TAG = "TESTING";
    public String upc;
    public String productName;
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
        upc = intent.getStringExtra("barcode");
        String var="";
        var = barcodeNum.getText().toString();
        barcodeNum.setText(var+upc);
        var = finalDate.getText().toString();
        finalDate.setText(var+upc);

        new GetItem().execute();

    }


    /**
     * <p>Title: GetItem.java</p>
     *
     * <p>Description: class called by the onCreate(), which contains all the methods and code
     * needed to retrieve the data from the requested URL and parse the JSON to use the
     * information stored in it for application use and display.</p>
     *
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
                //in the Baked Goods category. That data will then get parsed to be used in this app.
                URL url = new URL("https://www.datakick.org/api/items/"+upc);
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

            Log.d(TAG, "result ="+result);
            return null;
        }
        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);

            if (result != null) {
                Log.d(TAG, "about to start the JSON parsing" + result);
                try {
                    // code to parse the JSON here
                    JSONObject jsonObj = new JSONObject(result);
                     productName = jsonObj.getString("name");
                    String temp = itemName.getText().toString();
                    itemName.setText(temp + productName);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

        }
    }

}
