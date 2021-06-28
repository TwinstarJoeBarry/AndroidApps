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
package edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;
import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;

@SuppressWarnings("unused")
public class NestDBDataSource {

    public static final String LOG_TAG = NestDBDataSource.class.getSimpleName();

    private final SQLiteDatabase db;

    /**
     * Private constructor so that this class can only be created through the static-nested
     * {@link NestDBActivity} class.
     */
    // NOTE: Do not change the access modifier of this constructor.
    private NestDBDataSource(Context context) throws SQLException {

        /* Moving this call out of the constructor will most definitely cause issues with other
         * classes. See edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource.NestDBActivity */
        db = NestDBOpenHelper.getInstance(context).getWritableDatabase();

    }

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
        return db.insert("nestUPCs", null, values);
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
     * looks up the related product id based upon the category id, item name, and item subtitle
     * @param categoryId  The product's categoryId
     * @param name  The product's name
     * @param subtitle The product's subtitle
     * @return if found, the product id; -1 otherwise
     */
    public int getProdIdfromProdInfo(int categoryId, String name, String subtitle) {

        int pId = -1;

        String qry = "SELECT * FROM products WHERE categoryId = ? AND " +
                "upper(name) = upper(?) AND upper(subtitle) = upper(?)";

        // Use String.valueOf() to convert null objects to "null" as a string
        Cursor c = db.rawQuery(qry, new String[]{
                String.valueOf(categoryId), String.valueOf(name), String.valueOf(subtitle)});

        if (c.moveToFirst()) {

            // line below for testing purposes
            Log.d("DBASE", "data: " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));

            pId = c.getInt(c.getColumnIndex("id"));

        }

        c.close();

        return pId;

    }

    /**
     * getCategories
     *
     * TODO
     *
     * @return ArrayList<String> containing the categories
     */
    public ArrayList<String> getCategories() {

        // Create an empty list to store the categories into
        ArrayList<String> categories = new ArrayList<>();

        // (* = all, categories = table name)
        Cursor cursor = db.rawQuery("SELECT * FROM categories", new String[]{});

        /* Get the index of the "description" column. This column stores both name and subcategory
         * columns concatenated */
        final int DESCRIPTION_INDEX = cursor.getColumnIndex("description");

        // While we are not after the last row
        for (cursor.moveToFirst(); !cursor.isAfterLast();) {

            // Get the String stored in the "description" column, add it to the list
            categories.add(cursor.getString(DESCRIPTION_INDEX));

            // Move to the next row
            cursor.moveToNext();

        }

        // Make sure to close the cursor to release all of its resources
        cursor.close();

        // Return the list
        return categories;

    }


    /**
     * getNames
     *
     * TODO
     *
     * @param categoryId
     * @return
     */
    public ArrayList<String> getProductNames(int categoryId) {

        // Create an empty list to store the product names into
        ArrayList<String> names = new ArrayList<>();

        // (* = all columns, products = table name)
        Cursor cursor = db.rawQuery("SELECT * FROM products WHERE categoryId = ?",
                new String[]{ String.valueOf( categoryId ) });

        // Get the index of the "name" column, "this column stores the actual name of the category"
        final int NAME_INDEX = cursor.getColumnIndex("name");

        // While we are not after the last row
        for (cursor.moveToFirst(); !cursor.isAfterLast();) {

            String productName = cursor.getString(NAME_INDEX);

            // If the productName is not null and has not already been added to the list
            if (!cursor.isNull(NAME_INDEX) && !productName.equals("null") &&
                    !names.contains(productName))

                // Add the product name to the list
                names.add(productName);

            Log.d("TESTING", cursor.getString(NAME_INDEX));

            // Move to the next row
            cursor.moveToNext();

        }

        // Make sure to close the cursor to release all of its resources
        cursor.close();

        // Return the list of product names
        return names;

    }

    /**
     * getSubtitles
     *
     * TODO
     *
     * @param categoryId
     * @param productName
     * @return
     */
    public ArrayList<String> getProductSubtitles(int categoryId, String productName) {

        // Create an empty list to store the product's subtitles into
        ArrayList<String> subtitles = new ArrayList<>();

        // (* = all columns, products = table name)
        Cursor cursor = db.rawQuery(
                "SELECT * FROM products WHERE categoryId = ? AND upper(name) = upper(?)",
                new String[]{ String.valueOf(categoryId), productName });

        // Get the index of the "subtitle" column, "this column stores the actual subtitle of the product"
        final int SUBTITLE_INDEX = cursor.getColumnIndex("subtitle");

        // While we are not after the last row
        for (cursor.moveToFirst(); !cursor.isAfterLast();) {

            String productSubtitle = cursor.getString(SUBTITLE_INDEX);

            // If the subtitle is not null
            if (!cursor.isNull(SUBTITLE_INDEX) && !productSubtitle.equals("null"))

                // Get the String stored in the "subtitle" column, add it to the list
                subtitles.add(productSubtitle);

            // Move to the next row
            cursor.moveToNext();

        }

        // Make sure to close the cursor to release all of its resources
        cursor.close();

        // Return the list
        return subtitles;

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

    /**
     * Loads a {@link NestDBDataSource} object using a {@link BackgroundTask} when this Activity is
     * created ({@link NestDBActivity#onCreate}). Also has a method that returns the
     * fully-loaded/non-{@code null} {@link NestDBDataSource} object.
     */
    public abstract static class NestDBActivity extends AppCompatActivity {

        ///////////////////////////////////// CLASS VARIABLES //////////////////////////////////////

        /** The tag to use when printing to the log from this class. */
        public static final String LOG_TAG = NestDBActivity.class.getSimpleName();

        /** TaskHelper object that uses a single thread executor */
        private final TaskHelper taskHelper = new TaskHelper();

        private NestDBDataSource nestDBDataSource = null;

        private Future<NestDBDataSource> future = null;

        private static final long LOAD_DELAY = 1000L;

        //////////////////////////////////// LIFECYCLE METHODS /////////////////////////////////////

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_nest_db);

            setSupportActionBar(findViewById(R.id.nest_db_toolbar));

            /* If a task hasn't been submitted or an existing task has been cancelled before it
             * finished */
            if (future == null || future.isCancelled()) {

                // Create a loading dialog to display to the user
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Set the positive button's listener to null so we can use it later
                builder.setPositiveButton("DISMISS", null);

                builder.setView(R.layout.dialog_loading_database);

                // Make sure this is dialog is not cancelable so the user is forced to wait
                builder.setCancelable(false);

                final AlertDialog loadDialog = builder.create();

                loadDialog.show();

                // Hide the positive button of the dialog until it is needed
                loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);

                /* Submit a new BackgroundTask to the helper that loads the database,
                 * store the resulting future */
                future = taskHelper.submit(new BackgroundTask<Void, NestDBDataSource>() {

                    @Override
                    @WorkerThread
                    protected NestDBDataSource doInBackground() throws Exception {

                        // This allows the user to see the loading bar for a short time
                        // Optional: This can be removed if not needed/desired
                        Thread.sleep(LOAD_DELAY);

                        return new NestDBDataSource(NestDBActivity.this);

                    }

                    @Override
                    @MainThread
                    protected void onPostExecute(NestDBDataSource nestDBDataSource) {

                        // Update our instance variable since the database was successfully loaded
                        NestDBActivity.this.nestDBDataSource = nestDBDataSource;

                        // Switch the displayed view to the second child view
                        ((ViewFlipper) Objects.requireNonNull(
                                loadDialog.findViewById(R.id.dialog_flipper))).setDisplayedChild(1);

                        // Display the positive button to the user
                        loadDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setVisibility(View.VISIBLE);

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
                    @MainThread
                    protected void onError(@NonNull Throwable throwable) {
                        super.onError(throwable);

                        // Failed to load the database so make sure source object is set to null
                        NestDBActivity.this.nestDBDataSource = null;

                        // Switch the displayed view to the third child view
                        ((ViewFlipper) Objects.requireNonNull(
                                loadDialog.findViewById(R.id.dialog_flipper))).setDisplayedChild(2);

                        // Display the positive button to the user
                        loadDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setVisibility(View.VISIBLE);

                        loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                                "DISMISS", (dialog, which) -> {

                                    try {

                                        // Send the error to the outer NestDBActivity class
                                        NestDBActivity.this.onLoadError(throwable);

                                    } finally {

                                        dialog.dismiss();

                                    }

                                });

                    }

                });

            }

        }

        /**
         * Called after the user has been informed that the Nest.db database has been successfully
         * loaded/created.
         *
         * <p><br>
         * NOTE: Use this method instead of {@link #onCreate(Bundle)} to set the content view
         * </p>
         *
         * @param nestDBDataSource The database source object used to interact with the database
         */
        protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) { }

        /**
         * Called after the user has been informed that there was an error loading the Nest.db
         * database. Calls {@link #finish()} on this Activity. Can be overridden for additional
         * functionality.
         */
        protected void onLoadError(@NonNull Throwable throwable) {

            Log.w(LOG_TAG, "Finishing activity due to load error");

            finish();

        }

        ///////////////////////////////////// CLASS METHODS ////////////////////////////////////////

        /**
         * Returns the {@link NestDBDataSource} object stored in the {@code nestDBDataSource}
         * instance variable.
         *
         * <p><br>
         * Note: Checking the return value of isDatabaseLoaded() before calling this
         * method will help ensure that this method will not throw a {@link NullPointerException}.
         * </p>
         *
         * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
         * variable
         *
         * @see #isDatabaseLoaded()
         */
        @NonNull
        public final NestDBDataSource requireDataSource() {

            return Objects.requireNonNull(nestDBDataSource,
                    "The Nest.db database has not been successfully loaded yet");

        }

        /**
         * Static version of requireDataSource(). Returns the {@link NestDBDataSource} object stored
         * in the {@link NestDBActivity} that is associated with the given {@link Fragment}.
         *
         * <p><br>
         * Note: Checking the return value of isDatabaseLoaded() before calling this
         * method will help ensure that this method will not throw a {@link NullPointerException}.
         * </p>
         *
         * @param fragment {@link Fragment} that is associated with a {@link NestDBActivity}
         * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
         * variable
         * @throws ClassCastException If the FragmentActivity associated with the given Fragment is
         * not an instance of {@link NestDBActivity}
         *
         * @see #requireDataSource()
         * @see #isDatabaseLoaded()
         */
        @NonNull
        public static NestDBDataSource requireDataSource(@NonNull Fragment fragment) {

            FragmentActivity fragmentActivity = fragment.requireActivity();

            if (fragmentActivity instanceof NestDBActivity)

                return ((NestDBActivity) fragmentActivity).requireDataSource();

            throw new ClassCastException("The FragmentActivity this Fragment is currently " +
                    "associated with is not an instance of " +
                    NestDBActivity.class.getCanonicalName());

        }

        /**
         * Determines whether or not the task has finished. Does NOT determine if the task was
         * successful or not.
         *
         * @return {@code true} if the task has finished, {@code false} otherwise
         */
        public final boolean taskFinished() {

            return (future != null && future.isDone());

        }

        /**
         * Determines whether or not the Nest.db database has been successfully loaded.
         *
         * @return {@code true} if the database was successfully loaded, {@code false} otherwise
         */
        public final boolean isDatabaseLoaded() {

            return (taskFinished() && nestDBDataSource != null);

        }

    }

}

