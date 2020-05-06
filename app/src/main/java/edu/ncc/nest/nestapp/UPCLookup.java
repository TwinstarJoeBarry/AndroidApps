package edu.ncc.nest.nestapp;
/*
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
 *
 * U.S. Department of Agriculture, Agricultural Research Service. FoodData Central, 2019. fdc.nal.usda.gov.
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * An Activity that takes a UPC and finds its corresponding FDCID.
 *
 * @author Matthew Vitelli, Andrew Stager
 */
public class UPCLookup extends AppCompatActivity {
    private HashMap<String, String> upcMap; // keys are UPC, values are FDCID
    private String upc;
    private String fdcid;
    private EditText upcInput;
    private TextView fdcidText, usdaText;

    private static final String API_KEY = "DJ7sr2PMzfeIJCdvwYaPhHYTD2uQ3IKU0O9RrQu0"; // THE MAINTAINERS OF THIS PROJECT SHOULD GENERATE
    // AN API KEY FOR THIS PROJECT, THIS IS A STUDENTS API KEY.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_c_lookup);

        upcInput = findViewById(R.id.upcEditText);
        fdcidText = findViewById(R.id.fdcidText);
        usdaText = findViewById(R.id.usdaText);

        // if the last activity has a barcode to give this one, search it
        Intent intent = getIntent();
        if (intent.getStringExtra("barcode") != null) {
            upcInput.setText(intent.getStringExtra("barcode"));
            search(null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate) {
        outstate.putSerializable("upcMap", upcMap);
        outstate.putString("upc", upc);
        outstate.putString("fdcid", fdcid);
        outstate.putString("upcInput", upcInput.getText().toString());
        outstate.putString("fdcidText", fdcidText.getText().toString());
        outstate.putString("usdaText", usdaText.getText().toString());
        super.onSaveInstanceState(outstate);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        upcMap = (HashMap) savedInstanceState.getSerializable("upcMap");
        upc = savedInstanceState.getString("upc");
        fdcid = savedInstanceState.getString("fdcid");
        upcInput.setText(savedInstanceState.getString("upcInput"));
        fdcidText.setText(savedInstanceState.getString("fdcidText"));
        usdaText.setText(savedInstanceState.getString("usdaText"));
    }

    /**
     * Creates the HashMap that stores the UPC and FDCID codes.
     */
    public void createLookupTable() {
        fdcidText.setText(R.string.upc_lookup_generating_table);
        // Parse .csv to create hashmap of UPC and FDCID
        upcMap = new HashMap<String, String>();
        try {
            InputStream csvInputStream = getResources().openRawResource(R.raw.fdcid_to_upc);
            InputStreamReader csvStreamReader = new InputStreamReader(csvInputStream);
            BufferedReader csvBuffReader = new BufferedReader(csvStreamReader);

            String line;
            while ((line = csvBuffReader.readLine()) != null) {
                String[] splitLine = line.split(",");
                upcMap.put(splitLine[1], splitLine[0]); // The csv file is flipped; UPCs are on the right and FDCIDs are on the left.
            }
            csvBuffReader.close();
            csvStreamReader.close();
            csvInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when you press the scan button.  Starts up the activity that uses the camera.
     *
     * @param v unused
     */
    public void scan(View v) {
        Intent upcScanner = new Intent(this, UPCScanner.class);
        startActivity(upcScanner);
    }

    /**
     * Called when the search button is pressed.  Looks up the UPC code in the text box and sets the text view to the corresponding FDCID code.
     *
     * @param v unused
     */
    public void search(View v) {
        upc = upcInput.getText().toString().substring(1); // get rid of an extra zero
        
        if (upc.length() != 11) {
            fdcidText.setText(R.string.upc_lookup_bad_upc);
            usdaText.setText("");
            fdcid = "";
            return;
        }

        // This is here because if we put it in onCreate, it will remake itself every time.
        if (upcMap == null) {
            createLookupTable();
        }

        fdcid = upcMap.get(upc);
        if (fdcid == null) {
            fdcidText.setText(R.string.upc_lookup_no_match_found);
            usdaText.setText("");
        } else {
            fdcidText.setText(getString(R.string.upc_lookup_match_found, fdcid));
            usdaText.setText(R.string.upc_lookup_retreiving_json);
            new JSONGetter().execute();
        }
    }

    /**
     * Class for pulling from the USDA database.  It runs asynchronously from everything else in this class to prevent freezing while looking up.
     */
    private class JSONGetter extends AsyncTask<Void, Void, Void> {
        String result = "";

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection httpConnection;
            BufferedReader httpBuffReader;

            try {
                // set the URL for the API call
                URL url = new URL("https://api.nal.usda.gov/fdc/v1/food/" + fdcid + "?api_key=" + API_KEY);
                // connect to the site to read information
                httpConnection = (HttpsURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();
                InputStream httpInputStream = httpConnection.getInputStream();

                if (httpInputStream == null) {
                    usdaText.setText(R.string.upc_lookup_usda_no_response);
                    return null;
                }
                // store the data into a BufferedReader so it can be stored into a string
                InputStreamReader httpInStreamReader = new InputStreamReader(httpInputStream);
                httpBuffReader = new BufferedReader(httpInStreamReader);

                StringBuilder resultBuilder = new StringBuilder();
                String line;
                while ((line = httpBuffReader.readLine()) != null) {
                    resultBuilder.append(line);
                }
                result = resultBuilder.toString();
                httpBuffReader.close();
                httpInStreamReader.close();
                httpInputStream.close();
                httpConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (result == null) {
                usdaText.setText(R.string.upc_lookup_usda_null_response);
                return;
            }
            try {
                JSONObject resultJSON = new JSONObject(result);
                usdaText.setText(resultJSON.getString("brandedFoodCategory"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
