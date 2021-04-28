/**
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 * <p>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package edu.ncc.nest.nestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;

public class NestDBDataSource {
    private SQLiteDatabase db;
    private NestDBOpenHelper helper;
    public static String TABLE_NAME_NEST_UPCS = "nestUPCs";
    public String TAG = "TESTING";

    private NestDBDataSource(Context context) throws SQLException {
        helper = NestDBOpenHelper.getInstance(context);

        /** Moving this call out of the constructor will most definitely cause issues with other classes.
         *  See {@link NestDBActivity} */
        this.db = helper.getWritableDatabase();

    }

//  opting to not have these methods...
//    public void open() throws SQLException {
//        // why have an open? since we don't need a close, just get the db as part of the constructor
//    }
//
//    public void close() {
//        // as NestDBOpenHelper is Singleton, do not call itd close() method; close not required anyway
//        // helper.close();
//    }

    /**
     * insertNewUPC method --
     * adds a new record the Nest UPCs table
     *
     * @param upc         upc code
     * @param brand       brand name
     * @param description description
     * @param productId   associated FoodKeeper product id
     * @return The row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertNewUPC(String upc, String brand, String description, int productId) {
        ContentValues values = new ContentValues();
        values.clear();
        values.put("upc", upc);
        values.put("brand", brand);
        values.put("description", description);
        values.put("productId", productId);
        return db.insert(TABLE_NAME_NEST_UPCS, null, values);
    }

    /**
     * getProductIdFromUPC method -
     * looks up the given UPC code in the Nest UPCs table and returns the associated
     * product ID
     *
     * @param upc UPC code
     * @return integer productId field value if found, -1 otherwise
     */
    public int getProductIdFromUPC(String upc) {
        int result = -1; // indicate not found
        String qry = "SELECT productId FROM nestUPCs WHERE upper(UPC) = upper(?)";
        Cursor c = db.rawQuery(qry, new String[]{upc});
        if (c.moveToFirst()) {
            result = c.getInt(0);
        }
        c.close();
        return result;
    }

    /**
     * getNestUPC method --
     * looks up the related UPC, product and category information based
     * on the given upc
     *
     * @param upc upc code
     * @return if found, a filled NestUPC pojo with all
     * the key related information; null otherwise.
     */
    public NestUPC getNestUPC(String upc) {
        NestUPC result = null;
        String qry = "SELECT * FROM view_upc_product_category_joined WHERE upper(UPC) = upper(?)";
        Cursor c = db.rawQuery(qry, new String[]{upc});
        if (c.moveToFirst()) {
            result = new NestUPC(
                    c.getString(c.getColumnIndex("UPC")),
                    c.getString(c.getColumnIndex("brand")),
                    c.getString(c.getColumnIndex("description")),
                    c.getInt(c.getColumnIndex("productId")),
                    c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("subtitle")),
                    c.getInt(c.getColumnIndex("categoryId")),
                    c.getString(c.getColumnIndex("cat_desc"))
            );
        }
        c.close();
        return result;
    }

    /**
     * getProdIdfromProdInfo method --
     * looks up the related product id based upon the category id and item name
     * @param cId  the category id
     * @param iName  the item name    *
     * @return if found, the product id; -1 otherwise
     */
    public int getProdIdfromProdInfo(int cId, String iName) {
        int pId = -1;

        String qry = "SELECT * FROM products WHERE categoryId = " + cId + " AND upper(name) = upper(?)";

        Cursor c = db.rawQuery(qry, new String[]{iName});
        if (c.moveToFirst()) {
            // line below for testing purposes
            Log.d("DBASE", "data: " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
            pId = c.getInt(c.getColumnIndex("id"));
        }
        c.close();
        return pId;
    }


    /**
     * getShelfLivesForProduct method --
     * looks up the shelf life records for the given productId
     *
     * @param productId the FoodKeeper product id to lookup
     * @return an ArrayList<ShelfLife> object, which will have no
     * contents if nothing is found
     */
    public List<ShelfLife> getShelfLivesForProduct(int productId) {
        List<ShelfLife> result = new ArrayList<>();
        String qry = "SELECT * FROM view_shelf_lives_and_type_info_joined WHERE productId = ?";
        Cursor c = db.rawQuery(qry, new String[]{String.valueOf(productId)});
        while (c.moveToNext()) {
            ShelfLife life = new ShelfLife(
                    c.getInt(c.getColumnIndex("typeIndex")),
                    c.getInt(c.getColumnIndex("min")),
                    c.getInt(c.getColumnIndex("max")),
                    c.getString(c.getColumnIndex("metric")),
                    c.getString(c.getColumnIndex("tips"))
            );
            life.setCode(c.getString(c.getColumnIndex("typeCode")));
            life.setDesc(c.getString(c.getColumnIndex("description")));
            result.add(life);
        }
        c.close();
        return result;
    }

    public static abstract class NestDBActivity extends AppCompatActivity {

        public static final String LOG_TAG = NestDBActivity.class.getSimpleName();

        private final TaskHelper taskHelper = new TaskHelper(1);

        private NestDBDataSource nestDBDataSource = null;

        private Future<NestDBDataSource> future = null;

        private static final long LOAD_DELAY = 1000L;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Create a loading dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the positive button's listener to null so we can use it later
            builder.setPositiveButton("DISMISS", null);

            builder.setView(R.layout.dialog_loading_database);

            builder.setCancelable(false);

            AlertDialog loadDialog = builder.create();

            loadDialog.show();

            // Hide the "dismiss" button until it is needed
            loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);

            if (future == null || future.isCancelled())

                // Submit a new BackgroundTask to the helper that loads the database, store the resulting future
                future = taskHelper.submit(new BackgroundTask<Void, NestDBDataSource>() {

                    @Override
                    protected NestDBDataSource doInBackground() throws Exception {

                        try {

                            // This allows the user to see the loading bar for a short time
                            // Optional: This can be removed if not needed/desired
                            Thread.sleep(LOAD_DELAY);

                        } catch (InterruptedException e) {

                            Log.w(LOG_TAG, "Non-Fatal Exception occurred");

                            Log.w(LOG_TAG, Log.getStackTraceString(e));

                        }

                        return new NestDBDataSource(NestDBActivity.this);

                    }

                    @Override
                    protected void onPostExecute(NestDBDataSource nestDBDataSource) {

                        // Update our instance variable since the database was successfully loaded
                        NestDBActivity.this.nestDBDataSource = nestDBDataSource;

                        // Switch the displayed view to the first child view
                        ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper)).setDisplayedChild(1);

                        // Display the positive button to the user
                        loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                        loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "DISMISS", (dialog, which) -> {

                            try {

                                // Send the result to the outer class
                                NestDBActivity.this.onLoadSuccess(nestDBDataSource);

                            } finally {

                                dialog.dismiss();

                            }

                        });

                    }

                    @Override
                    protected void onError(@NonNull Throwable throwable) {

                        // Failed to load the database so make sure source object is set to null
                        NestDBActivity.this.nestDBDataSource = null;

                        // Switch the displayed view to the second child view
                        ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper)).setDisplayedChild(2);

                        // Display the positive button to the user
                        loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                        loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "DISMISS", (dialog, which) -> {

                            try {

                                NestDBActivity.this.onLoadError(throwable);

                            } finally {

                                dialog.dismiss();

                            }

                        });

                    }

                });

        }

        /**
         * Returns the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
         * variable.
         *
         * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
         * variable
         * @throws NullPointerException If the object is {@code null}/not-yet-loaded
         */
        @NonNull
        public final NestDBDataSource requireDataSource() {

            return Objects.requireNonNull(nestDBDataSource, "nestDBDataSource is null");

        }

        /**
         * Called after the user has been informed that the Nest.db database has been successfully
         * loaded/created.
         *
         * @param nestDBDataSource The database source object used to interact with the database
         */
        protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) {

        }

        /**
         * Called if there was an error while loading the database.
         *
         * NOTE: This method is called after the user is informed (by an {@link AlertDialog}) of the
         * error.
         */
        protected void onLoadError(@NonNull Throwable throwable) {

            throw new RuntimeException("Error loading Nest.db database", throwable);

        }

    }

}