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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class InventoryInfoSource {
    private SQLiteDatabase database;
    private InventoryInfoHelper inventoryHelper;

    private String[] allColumns = { InventoryInfoHelper._ID, InventoryInfoHelper.ITEM_ID, InventoryInfoHelper.ITEM_NAME,
            InventoryInfoHelper.PANTRY_LIFE, InventoryInfoHelper.BARCODE_NUM };
    private static String ORDER_BY = InventoryInfoHelper.ITEM_NAME;

    public InventoryInfoSource(Context context) {
        inventoryHelper = new InventoryInfoHelper(context);
    }

    public void open() throws SQLException {
        database = inventoryHelper.getWritableDatabase();
    }

    public void close() {
        inventoryHelper.close();
    }

    //removeItem() only to be used by a developer, not available to the users that will use the app
    public void removeRecord(){
        String id = "7";
        database.delete(InventoryInfoHelper.TABLE_NAME, InventoryInfoHelper._ID + " LIKE ? " ,new String[]{"%"+id+"%"});
    }

    //method used for insertion for adding a new record, with all its field values, to our local database
    public void addItem(String itemId, String itemName, String pantryLife, String upc) {
        ContentValues values = new ContentValues();
        values.put(InventoryInfoHelper.ITEM_ID, itemId);
        values.put(InventoryInfoHelper.ITEM_NAME, itemName);
        values.put(InventoryInfoHelper.PANTRY_LIFE, pantryLife);
        values.put(InventoryInfoHelper.BARCODE_NUM, upc);
        long insertId = database.insert(InventoryInfoHelper.TABLE_NAME, null, values);
    }
    //method used to set up the contents of our local database to display the full local database
    public List<InventoryEntry> getProducts() {
        List<InventoryEntry> products = new ArrayList<>();
        InventoryEntry entry;
        Cursor cursor = database.query(InventoryInfoHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            products.add(entry);
            cursor.moveToNext();
        }
        cursor.close();
        return products;
    }
    //method used to query the database to look for a matching UPC barcode number
    //method returns an ArrayList, with objects matching the barcode number
    //returns an empty List if no matching barcode number is found in the database
    public List<InventoryEntry> findProducts(String upc) {
        List<InventoryEntry> products = new ArrayList<>();
        InventoryEntry entry;

        //query by upc barcode num
        Cursor cursor = database.query(InventoryInfoHelper.TABLE_NAME,
                allColumns, InventoryInfoHelper.BARCODE_NUM + " LIKE ? ", new String[]{ upc+"%" },
                null, null,null);

        // uncomment the code below once the query call has been made
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            products.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        return products;
    }


    //method used to query the database to look for a matching item id
    //method returns an ArrayList, with object matching the item id
    //returns an empty List if no matching item id is found in the database
    public List<InventoryEntry> findProductsByItemId(String itemId) {
        List<InventoryEntry> products = new ArrayList<>();
        InventoryEntry entry;

        //query by upc barcode num
        Cursor cursor = database.query(InventoryInfoHelper.TABLE_NAME,
                allColumns, InventoryInfoHelper.ITEM_ID + " == ? ", new String[]{ itemId },
                null, null,null);

        // uncomment the code below once the query call has been made
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            products.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        return products;
    }



    private InventoryEntry cursorToEntry(Cursor cursor) {
        InventoryEntry entry = new InventoryEntry(cursor.getLong(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return entry;
    }

}
