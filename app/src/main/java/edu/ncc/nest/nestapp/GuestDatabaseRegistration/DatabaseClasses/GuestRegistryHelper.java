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
    //TODO Need:
    // 1. Age
    // 2. Gender
    // 3. Dietary Needs & Pref
    // 4. Has SNAP/food stamps
    // 5. Know other emergency food program
    // 6. Employment Status
    // 7. Health Status
    // 8. Housing Status
    // 9. Household Income
    // 10. How many people in household
    // 11. Childcare Status
    // 12. #Child Age situation:
    // a. Age < 1
    // b. 13 <= Age <= 5
    // c. 6 <= Age <= 12
    // d. 13 <= Age <= 18

    public static final String _ID = "_id";

    //first fragment information
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String NCC_ID = "nccID";
    public static final String DATE = "date";


    //second fragment information
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String ZIP = "zipcode";
    public static final String STATE = "state";
    public static final String AFFILIATION = "affiliation";
    public static final String AGE = "age";
    public static final String GENDER = "gender";

    //third fragment information
    private static String DIET = "diet";
    private static String PROGRAMS = "programs";
    private static String SNAP = "snap";
    private static String EMPLOYMENT = "employment";
    private static String HEALTH = "health";
    private static String HOUSING = "housing";
    private static String INCOME = "income";

    //fourth fragment information
    private static String HOUSEHOLD_NUM = "householdNum";
    private static String CHILDCARE_STATUS = "childcareStatus";
    private static String CHILDREN_1 = "children1";
    private static String CHILDREN_5 = "children5";
    private static String CHILDREN_12 = "children12";
    private static String CHILDREN_18 = "children18";


    //TODO: additional data to be added when the data when UI has it
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
