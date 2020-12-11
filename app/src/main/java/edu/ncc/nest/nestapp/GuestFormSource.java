package edu.ncc.nest.nestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GuestFormSource
{
    private SQLiteDatabase database;
    private GuestFormHelper guestHelper;

    /**
     * Default Constructor --
     * @param context
     */
    public GuestFormSource( Context context )
    {
        guestHelper = new GuestFormHelper( context );
    }

    /**
     * open method -- opens a writeable database
     * @throws SQLException
     */
    public void open( ) throws SQLException
    {
        database = guestHelper.getWritableDatabase( );
    }

    /**
     * close method --
     * closes a database
     */
    public void close( )
    {
        guestHelper.close( );
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
     * @param zip - the zipcode of the guests' address
     * @param barcode - the barcode that belongs to the guest
     * @return true if the data has been inserted without issue, false otherwise
     */
    public boolean insertData(String name, String email, String phone, String date, String address, String city, String zip, String barcode)
    {
        //All info for a single user will be placed into the same ContentValue variable (Key & Value map-like variable)
        ContentValues cValues = new ContentValues();

        //loading user information into the content value
        cValues.put(guestHelper.NAME, name);
        cValues.put(guestHelper.EMAIL, email);
        cValues.put(guestHelper.PHONE, phone);
        cValues.put(guestHelper.DATE, date);
        cValues.put(guestHelper.ADDRESS, address);
        cValues.put(guestHelper.CITY, city);
        cValues.put(guestHelper.ZIP, zip);
        cValues.put(guestHelper.BARCODE, barcode);
        // leaving out for now: cValues.put(STATE, state);

        //placing the information into the database
        long res = database.insert(guestHelper.TABLE_NAME, null, cValues);

        //insert method will return a negative 1 if there was an error with the insert
        if (res == -1) {
            return false;
        }

        //returning true if insertion was successful
        return true;
    }



    // More methods...

} //End of GuestFormSource class

