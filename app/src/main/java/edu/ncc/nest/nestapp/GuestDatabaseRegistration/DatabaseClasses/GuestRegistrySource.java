package edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses;

/**
 *
 * Copyright (C) 2020 The LibreFoodPantry Developers.
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

import androidx.annotation.NonNull;

import static edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistryHelper.BARCODE;
import static edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistryHelper.NAME;
import static edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistryHelper.TABLE_NAME;

/**
 * GuestRegistrySource: Handles the insertion and removal of data into the GuestRegistry database
 * (See {@link GuestRegistryHelper}). Also has methods that allow for searching the database.
 *
 */
public class GuestRegistrySource {
    
    private final GuestRegistryHelper registryHelper;

    private final SQLiteDatabase database;

    /**
     * Default Constructor --
     * @param context
     */
    public GuestRegistrySource(Context context) {

        registryHelper = new GuestRegistryHelper(context);

        // Open a database that will be used for reading and writing
        database = registryHelper.getWritableDatabase();

    }

    /**
     * close --
     * Closes the database.
     */
    public void close( ) {

        database.close();

        registryHelper.close();

    }

    /**
     * insertData method --
     * Takes the data passed as arguments and inserts it into the database appropriately.
     * If the data is inserted without issue, the value for the row ID will be returned,
     * and in turn the method will return a boolean value of true. If an issue does occur,
     * -1 will be returned and the method will return a boolean value of false.
     *
     * @param name - the name of the guest
     * @param email - the email of the guest
     * @param phone - the phone number of the guest
     * @param date - the date that the form has been filled out
     * @param address - the address of the guest
     * @param city - the city of the guests' address
     * @param zip - the zip-code of the guests' address
     * @param barcode - the barcode that belongs to the guest
     * @return true if the data has been inserted without issue, false otherwise
     */
    // FIXME Needs to be updated to match all columns of the database
    public boolean insertData(String name, String email, String phone, String date, String address, String city, String zip, String barcode) {

        //All info for a single user will be placed into the same ContentValue variable (Key & Value map-like variable)
        ContentValues cValues = new ContentValues();

        //loading user information into the content value
        cValues.put(GuestRegistryHelper.NAME, name);
        cValues.put(GuestRegistryHelper.EMAIL, email);
        cValues.put(GuestRegistryHelper.PHONE, phone);
        cValues.put(GuestRegistryHelper.DATE, date);
        cValues.put(GuestRegistryHelper.ADDRESS, address);
        cValues.put(GuestRegistryHelper.CITY, city);
        cValues.put(GuestRegistryHelper.ZIP, zip);
        cValues.put(GuestRegistryHelper.BARCODE, barcode);
        // leaving out for now: cValues.put(STATE, state);

        // Insert method will return a negative 1 if there was an error with the insert
        return database.insert(TABLE_NAME, null, cValues) != -1;

    }

    /**
     * isRegistered - Takes 1 parameter
     * Determines whether or not a guest is currently registered with the provided barcode
     * @param barcode - The barcode to search the database for
     * @return Returns the name of the first guest who is registered in the database with the barcode
     * or null if there is no guest registered with that barcode
     */
    public String isRegistered(@NonNull String barcode) {

        if (database.isOpen()) {

            // Get all the guest records from the table (TABLE_NAME) who's field name (BARCODE) matches the field value (?).
            String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + BARCODE + " = ?";

            // Run the SQL query from above and replace the '?' character with the respective argument stored in the String[] array
            Cursor cursor = database.rawQuery(sqlQuery, new String[]{barcode});

            String name = null;

            // Determine if there is at least 1 guest registered with the barcode and get the name of the first person registered with it
            if (cursor.moveToFirst())

                name = cursor.getString(cursor.getColumnIndex(NAME));

            // Close the cursor to release all of its resources
            cursor.close();

            return name;

        } else

            throw new RuntimeException("Cannot query a closed database.");

    }


    @Override // Called by the garbage collector
    protected void finalize() throws Throwable {

        // Make sure we close the writable database before this object is finalized
        database.close();

        super.finalize();

    }

}
