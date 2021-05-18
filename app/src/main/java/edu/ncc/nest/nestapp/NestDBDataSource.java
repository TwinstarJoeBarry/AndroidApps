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
import android.util.Log;
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

public class NestDBDataSource {
    private SQLiteDatabase db;
    private NestDBOpenHelper helper;
    public static String TABLE_NAME_NEST_UPCS = "nestUPCs";
    public String TAG = "TESTING";


    public NestDBDataSource(Context context) throws SQLException {
        helper = NestDBOpenHelper.getInstance(context);

        /** Moving this call out of the constructor will most definitely cause issues with other classes.
         *  See {@link edu.ncc.nest.nestapp.CheckExpirationDate.Fragments.ScannerFragment} */
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
     * @param categoryId  The product's categoryId
     * @param name  The product's name
     * @param subtitle The product's subtitle
     * @return if found, the product id; -1 otherwise
     */
    public int getProdIdfromProdInfo(int categoryId, String name, String subtitle) {

        int pId = -1;

        String qry = "SELECT id FROM products WHERE categoryId = ?" +
                " AND upper(name) = upper(?) AND upper(subtitle) = ?";

        Cursor c = db.rawQuery(qry, new String[]{ String.valueOf(categoryId), name, subtitle });

        if (c.moveToFirst()) {

            // line below for testing purposes
            Log.d("DBASE", "data: " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));

            pId = c.getInt(0);

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

            // If the productName has not already been added to the list
            if (!names.contains(productName))

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

            // Get the String stored in the "subtitle" column, add it to the list
            subtitles.add(cursor.getString(SUBTITLE_INDEX));

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
    public List<ShelfLife> getShelfLivesForProduct(int productId)
    {
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

}