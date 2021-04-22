package edu.ncc.nest.nestapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;

/**
 *

 Copyright (C) 2020 The LibreFoodPantry Developers.


 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.


 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.


 You should have received a copy of the GNU General Public License
 along with this program.  If not, see https://www.gnu.org/licenses/.
 */

/**
 * This class uses the Singleton pattern to ensure only one instance of it
 * is used throughout the application.  This allows multiple threads to access
 * the database while automatically providing synchronization of database
 * operations (which is built into SQLiteopenHelper).  To use this class,
 * call its static getInstance() method.
 */
public class NestDBOpenHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = NestDBDataSource.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Nest.db";
    private static final String DATABASE_CREATE_SQL = "NestDB.Create.sql";
    private static final String DATABASE_DESTROY_SQL = "NestDB.Destroy.sql";
    private Context mContext;

    // keep static reference to single instance of this class (Singleton pattern)
    @SuppressLint("StaticFieldLeak")  // we use Application Context only (no memory leak)
    private static NestDBOpenHelper mInstance = null;


    private static final HashMap<String, String> taskResults = new HashMap<>();

    // getInstance static factory method (Singleton pattern)
    public static NestDBOpenHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new NestDBOpenHelper(context.getApplicationContext());
        return mInstance;
    }

    /**
     * Default constructor is private to prevent direct instantiation,
     * use the getInstance() factory method instead (Singleton pattern)
     * @param context application context
     */
    private NestDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try (Scanner scan = new Scanner(mContext.getAssets().open(DATABASE_CREATE_SQL))) {
            scan.useDelimiter(";"); // everything up to the next semicolon is the next statement
            while (scan.hasNext()) {
                String sql = scan.next().trim();
                // avoid trying to execute empty statements (execSQL doesn't like that)
                if (!sql.isEmpty()) {
                    Log.d(DATABASE_NAME, "sql statement is:\n" + sql);
                    db.execSQL(sql);
                } else {
                    Log.d(DATABASE_NAME, "sql statement is empty");
                }
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.wtf(DATABASE_NAME, DATABASE_NAME + " creation failed: " + e.getMessage(), e);
        }
        db.endTransaction();
        populateFromFoodKeeperAPI(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){
        db.beginTransaction();
        try (Scanner scan = new Scanner(mContext.getAssets().open(DATABASE_DESTROY_SQL))) {
            scan.useDelimiter(";"); // everything up to the next semicolon is the next statement
            while (scan.hasNext()) {
                String sql = scan.next().trim();
                // avoid trying to execute empty statements (execSQL doesn't like that)
                if (!sql.isEmpty()) {
                    Log.d(DATABASE_NAME, "sql statement is:\n" + sql);
                    db.execSQL(sql);
                } else {
                    Log.d(DATABASE_NAME, "sql statement is empty");
                }
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.wtf(DATABASE_NAME, DATABASE_NAME + " upgrade preparation failed: " + e.getMessage(), e);
        }
        db.endTransaction();
        onCreate(db);
    }

    private void populateFromFoodKeeperAPI(SQLiteDatabase db) {

        TaskHelper taskHelper = new TaskHelper(4);

        try {

            ArrayList<Future<?>> futures = new ArrayList<>();

            futures.add(taskHelper.submit(new GetCategoriesTask(db)));

            futures.add(taskHelper.submit(new GetCookingTipsTask(db)));

            futures.add(taskHelper.submit(new GetCookingMethodsTask(db)));

            futures.add(taskHelper.submit(new GetProductsTask(db)));

            if (Looper.getMainLooper().isCurrentThread()) {

                Log.w(LOG_TAG, "Populating database from main thread may cause UI to freeze");

                Toast.makeText(mContext, "Warning: Populating database from main thread",
                        Toast.LENGTH_LONG).show();

            }

            for (Future<?> future : futures) {

                try {

                    future.get();

                } catch (InterruptedException | ExecutionException e) {

                   e.printStackTrace();

                }

            }

        } finally {

            taskHelper.shutdown();

        }

    }

    /**
     * Inner class to retrieve all categories from the FoodKeeper API
     */
    private static class GetCategoriesTask extends BackgroundTask<Integer, String> {

        private final SQLiteDatabase db;

        public GetCategoriesTask(@NonNull SQLiteDatabase db) { this.db = db; }

        @Override
        protected String doInBackground() throws Exception {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            // set the URL for the API call
            String apiCall = "https://foodkeeper-api.herokuapp.com/categories";
            Log.d(DATABASE_NAME, "apiCall = " + apiCall);
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
            // store the data in result string
            return reader.readLine();

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d(DATABASE_NAME, "categories JSON result length = " + result.length());
                db.beginTransaction();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d(DATABASE_NAME, "processing " + jsonArray.length() + " categories array entries...");
                    ContentValues values = new ContentValues();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        values.clear();
                        values.put("id", jsonObj.getString("id"));
                        // todo can remove name & subcategory from table if decide to always use
                        //  "name (subcategory)" description like in ItemInformation activity
                        values.put("name", jsonObj.getString("name"));
                        if (!jsonObj.isNull("subcategory")) {
                            values.put("subcategory", jsonObj.getString("subcategory"));
                            values.put("description", jsonObj.getString("name")
                                    + " (" + jsonObj.getString("subcategory") + ")");
                        } else {
                            values.put("description", jsonObj.getString("name"));
                        }
                        db.insert("categories", null, values);
                        db.yieldIfContendedSafely();
                    }
                    db.setTransactionSuccessful();
                    Log.d(DATABASE_NAME, "inserted " + jsonArray.length() + " categories");

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            } else {
                Log.d(DATABASE_NAME, "Couldn't get any categories data from the url");
            }

        }

    }

    /**
     * Inner class to retrieve all cookingTips from the FoodKeeper API
     */
    private static class GetCookingTipsTask extends BackgroundTask<Integer, String> {

        private final SQLiteDatabase db;

        public GetCookingTipsTask(@NonNull SQLiteDatabase db) { this.db = db; }

        @Override
        protected String doInBackground() {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                String apiCall = "https://foodkeeper-api.herokuapp.com/cookingTips";
                Log.d(DATABASE_NAME, "apiCall = " + apiCall);
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
                // store the data in result string
                return reader.readLine();

            } catch (Exception e) {
                Log.d(DATABASE_NAME, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d(DATABASE_NAME, "cookingTips JSON result length = " + result.length());
                db.beginTransaction();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d(DATABASE_NAME, "processing " + jsonArray.length() + " cookingTips array entries...");
                    ContentValues values = new ContentValues();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        values.clear();
                        values.put("id", jsonObj.getString("id"));
                        values.put("tips", jsonObj.getString("tips"));
                        values.put("safeMinTemp", jsonObj.getString("safeMinTemp"));
                        values.put("restTime", jsonObj.getString("restTime"));
                        values.put("restTimeMetric", jsonObj.getString("restTimeMetric"));
                        values.put("productId", jsonObj.getString("productID"));
                        db.insert("cookingTips", null, values);
                        db.yieldIfContendedSafely();
                    }
                    db.setTransactionSuccessful();
                    Log.d(DATABASE_NAME, "inserted " + jsonArray.length() + " cookingTips");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            } else {
                Log.d(DATABASE_NAME, "Couldn't get any cookingTips data from the url");
            }

        }

    }

    /**
     * Inner class to retrieve all cookingmethods from the FoodKeeper API
     */
    private static class GetCookingMethodsTask extends BackgroundTask<Integer, String> {

        private final SQLiteDatabase db;

        public GetCookingMethodsTask(@NonNull SQLiteDatabase db) { this.db = db; }

        @Override
        protected String doInBackground() {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                String apiCall = "https://foodkeeper-api.herokuapp.com/cookingMethods";
                Log.d(DATABASE_NAME, "apiCall = " + apiCall);
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
                // store the data in result string
                return reader.readLine();

            } catch (Exception e) {
                Log.d(DATABASE_NAME, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d(DATABASE_NAME, "cookingMethods JSON result length = " + result.length());
                db.beginTransaction();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d(DATABASE_NAME, "processing " + jsonArray.length() + " cookingMethods array entries...");
                    ContentValues values = new ContentValues();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        values.clear();
                        values.put("id", jsonObj.getString("id"));
                        values.put("method", jsonObj.getString("method"));
                        values.put("measureFrom", jsonObj.getString("measureFrom"));
                        values.put("measureTo", jsonObj.getString("measureTo"));
                        values.put("sizeMetric", jsonObj.getString("sizeMetric"));
                        values.put("cookingTemp", jsonObj.getString("cookingTemp"));
                        values.put("timingFrom", jsonObj.getString("timingFrom"));
                        values.put("timingTo", jsonObj.getString("timingTo"));
                        values.put("timingMetric", jsonObj.getString("timingMetric"));
                        values.put("timingPer", jsonObj.getString("timingPer"));
                        values.put("productId", jsonObj.getString("productID"));
                        db.insert("cookingMethods", null, values);
                        db.yieldIfContendedSafely();
                    }
                    db.setTransactionSuccessful();
                    Log.d(DATABASE_NAME, "inserted " + jsonArray.length() + " cookingMethods");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            } else {
                Log.d(DATABASE_NAME, "Couldn't get any cookingMethods data from the url");
            }

        }

    }

    /**
     * Inner class to retrieve all products from the FoodKeeper API
     */
    private static class GetProductsTask extends BackgroundTask<Integer, String> {

        private final SQLiteDatabase db;

        public GetProductsTask(@NonNull SQLiteDatabase db) { this.db = db; }

        @Override
        protected String doInBackground() {

            HttpURLConnection urlConnection;
            BufferedReader reader;

            try {
                // set the URL for the API call
                String apiCall = "https://foodkeeper-api.herokuapp.com/products";
                Log.d(DATABASE_NAME, "apiCall = " + apiCall);
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
                // store the data in result string
                return reader.readLine();

            } catch (Exception e) {
                Log.d(DATABASE_NAME, "EXCEPTION in HttpAsyncTask: " + e.getMessage());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.d(DATABASE_NAME, "products JSON result length = " + result.length());
                db.beginTransaction();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d(DATABASE_NAME, "processing " + jsonArray.length() + " product array entries...");
                    // string arrays used for the different shelf life fields (db side and api json side)
                    String[] fields = { "min", "max", "metric", "tips"};
                    String[] dbPrefixes = { "pl", "paol", "rl", "raol", "ratl", "fl", "dop_pl", "dop_rl", "dop_fl" };
                    String[] jsonNames = {
                            "pantryLife",
                            "pantryAfterOpeningLife",
                            "refrigeratorLife",
                            "refrigerateAfterOpeningLife",
                            "refrigerateAfterThawingLife",
                            "freezerLife",
                            "dop_pantryLife",
                            "dop_refrigeratorLife",
                            "dop_freezerLife"
                    };
                    ContentValues values = new ContentValues();
                    ContentValues slValues = new ContentValues(); // for shelfLives population
                    int slRecordsAdded = 0;
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        values.clear();
                        values.put("id", jsonObj.getString("id"));
                        values.put("categoryId", jsonObj.getString("categoryId"));
                        values.put("name", jsonObj.getString("name"));
                        values.put("subtitle", jsonObj.getString("subtitle"));
                        values.put("keywords", jsonObj.getString("keywords"));
                        // handle all the different shelf life fields
                        for(int j = 0; j < jsonNames.length; j++) {
                            JSONObject jsonShelfLife = jsonObj.getJSONObject(jsonNames[j]);
                            slValues.clear();   // begin possible new shelfLives record
                            for(String f:fields) {
                                //(unComment to store shelf life values in products) String dbField = dbPrefixes[j] + "_" + f;
                                if (!jsonShelfLife.isNull(f)) {
                                    //(unComment to store shelf life values in products) values.put(dbField, jsonShelfLife.getString(f));
                                    slValues.put(f, jsonShelfLife.getString(f));
                                }
                            }
                            // add record to shelfLives if something to add
                            if (slValues.size() > 0) {
                                // fill product id, shelf life type code and index value and add record
                                slValues.put("productId", values.getAsString("id"));
                                slValues.put("typeCode", dbPrefixes[j].toUpperCase());
                                slValues.put("typeIndex", j);  // matches ShelfLife.java
                                db.insert("shelfLives", null, slValues);
                                db.yieldIfContendedSafely();
                                slRecordsAdded++;
                            }
                        }
                        db.insert("products", null, values);
                        db.yieldIfContendedSafely();
                    }
                    db.setTransactionSuccessful();
                    Log.d(DATABASE_NAME, "inserted " + jsonArray.length() + " products");
                    Log.d(DATABASE_NAME, "inserted " + slRecordsAdded + " shelfLives records");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            } else {
                Log.d(DATABASE_NAME, "Couldn't get any data from the url");
            }

        }

    }

}
