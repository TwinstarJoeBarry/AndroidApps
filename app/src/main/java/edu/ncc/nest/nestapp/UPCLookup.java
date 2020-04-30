package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class UPCLookup extends AppCompatActivity {
    private HashMap<String, String> upcMap;
    private String upc;
    private char[] upcArray;
    private String fdcid;
    private EditText upcInput;
    private TextView fdcidText, usdaText;
    private final String API_KEY = "DJ7sr2PMzfeIJCdvwYaPhHYTD2uQ3IKU0O9RrQu0"; // THE MAINTAINERS OF THIS PROJECT SHOULD GENERATE
    // AN API KEY FOR THIS PROJECT, THIS IS A STUDENTS API KEY.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_p_c_lookup);

        upcInput = findViewById(R.id.upcEditText);
        fdcidText = findViewById(R.id.fdcidText);
        usdaText = findViewById(R.id.usdaText);

        Toast.makeText(getApplicationContext(), "Creating UPC Lookup Table", Toast.LENGTH_SHORT).show();

        // Parse .csv to create hashmap of UPC and FDCID
        upcMap = new HashMap<String, String>();
        try {
            InputStream csvInputStream = getResources().openRawResource(R.raw.fdcid_to_upc);
            InputStreamReader csvStreamReader = new InputStreamReader(csvInputStream);
            BufferedReader csvBuffReader = new BufferedReader(csvStreamReader);

            String line;
            while ((line = csvBuffReader.readLine()) != null) {
                String[] splitLine = line.split(",");
                this.upcMap.put(splitLine[1], splitLine[0]);
            }
            csvBuffReader.close();
            csvStreamReader.close();
            csvInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d("INFO", "There are " + Integer.toString(this.upcMap.size()) + " objects in the HashMap.");
    }

    public void search(View v) {
        fdcid = "";
        upc = upcInput.getText().toString();
        upcArray = upc.toCharArray();

        // UPC must be 12 digits in length
        if (upcArray.length != 12) {
            Toast.makeText(getApplicationContext(), "Bad UPC", Toast.LENGTH_SHORT).show();
            return;
        }

        //  UPC's in the USDA database ommit the first digit.
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 12; i++) {
            sb.append(upcArray[i]);
        }
        upc = sb.toString();
        fdcid = upcMap.get(upc);

        if (fdcid == null) {
            fdcidText.setText("No match found");
            usdaText.setText("");
            return;
        }
        fdcidText.setText("Match found\nFDCID: " + fdcid);

        new getJSON().execute();
    }

    private class getJSON extends AsyncTask<Void, Void, Void> {
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
                    Toast.makeText(getApplicationContext(), "No response from USDA API", Toast.LENGTH_SHORT).show();
                    return null;
                }
                // store the data into a BufferedReader so it can be stored into a string
                httpBuffReader = new BufferedReader(new InputStreamReader(httpInputStream));

                String line;
                while ((line = httpBuffReader.readLine()) != null) {
                    result += line;
                }
                httpBuffReader.close();
                httpInputStream.close();
                httpConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("HttpAsyncTask", "EXCEPTION: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (result == null) {
                Toast.makeText(getApplicationContext(), "Bad response from USDA API", Toast.LENGTH_SHORT).show();
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
