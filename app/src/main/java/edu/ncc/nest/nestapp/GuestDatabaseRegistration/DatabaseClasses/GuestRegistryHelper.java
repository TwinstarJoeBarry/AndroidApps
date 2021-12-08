package edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses;

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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * GuestRegistryHelper: Handles the creation and upgrade of the GuestRegistry database.
 */
public class GuestRegistryHelper extends SQLiteOpenHelper {

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "edu.ncc.zqk.nestguestforms";

    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    // Table name
    public static final String TABLE_NAME = "NESTGuestRegistry";

    // Columns in the table
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String DATE = "date";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String ZIP = "zipcode";
    public static final String STATE = "state";
    public static final String NCC_ID = "nccID";
    public static final String ADDITIONAL_INFO = "addInfo";
    public static final String NAME_OF_VOLUNTEER = "nameOfVolunteer";
    public static final String BARCODE = "barcode";

    // Database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GuestRegistry.db";

    public GuestRegistryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " +
                DATE + " TEXT, " + ADDRESS + " TEXT, " + CITY + " TEXT, " + ZIP + " TEXT, " +
                STATE + " TEXT, " + ADDITIONAL_INFO + " TEXT, " + NAME_OF_VOLUNTEER + " TEXT, " +
                NCC_ID + " TEXT, " + PHONE + " TEXT, " + BARCODE + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }

}
