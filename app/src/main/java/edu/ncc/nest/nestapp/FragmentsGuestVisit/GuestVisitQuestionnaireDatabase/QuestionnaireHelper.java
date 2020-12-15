package edu.ncc.nest.nestapp.FragmentsGuestVisit.QuestionnaireDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * QuestionnaireHelper Class
 *
 * The helper file of the three database files. This file creates the table itself for the database
 * to have items stored in.
 *
 * Author: Charles Cohen (CCONX)
 */
public class QuestionnaireHelper extends SQLiteOpenHelper {

    public static final String TAG = QuestionnaireHelper.class.getSimpleName();

    // Database name and version
    private static final String DATABASE_NAME = "QuestionnaireSubmissions.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_NAME = "questionnaire_submissions";

    // Columns in the table
    public static final String ROW_ID = "row_id";
    public static final String GUEST_ID = "guest_id"; //Reference to the id num of the guest
    public static final String QUESTION_PREFIX = "question_";

    // Number of questions that the questionnaire ask
    private static int numQuestions;

    /**
     * QuestionnaireHelper constructor method.
     * This method is used in GuestQuestionnaireFragment to validate numQuestions.
     * @param context SQLite required param
     * @param numQuestions Number of questions passed through
     */
    public QuestionnaireHelper(Context context, int numQuestions) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // Make sure e update numQuestions before validating the column count
        QuestionnaireHelper.numQuestions = numQuestions;

        // Validate the table column count to make sure there is enough columns for the number of questions being asked
        validateColumnCount();

    }

    // Used elsewhere if we need to access database outside of GuestQuestionnaireFragment

    /**
     * QuestionnaireHelper secondary constructor method.
     * This method is used elsewhere if we need to access database outside of GuestQuestionnaireFragment.
     * @param context SQLite required param
     */
    public QuestionnaireHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //Create the SQLineDatabase
        SQLiteDatabase db = getReadableDatabase();

        //Update numQuestions to the getColumnCount of the database
        numQuestions = getColumnCount(db);

        //Close
        db.close();

    }


    ////////////// Lifecycle Methods Start //////////////

    /**
     * onCreate method
     * Method starts on the creation of the class, creating the SQLiteDatabase.
     * @param db the SQLiteDatabase
     */
    @Override
    @SuppressLint("DefaultLocale")
    public void onCreate(SQLiteDatabase db) { // Creates the database table

        // Build the sql
        StringBuilder sqlBuilder = new StringBuilder(" CREATE TABLE " + TABLE_NAME + " (" +
                ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GUEST_ID + " TEXT");

        // For each question append the question prefix, question number, and the type
        for (int i = 1; i <= numQuestions; i++)

            sqlBuilder.append(String.format(", %s%d TEXT", QUESTION_PREFIX, i));

        // Make sure we append ); to the end of the sql, Then exec it
        db.execSQL(sqlBuilder.toString() + ");");

    }

    /**
     * onUpgrade method
     * When a new version of the app is created, drop the old table and create a new one
     * @param db SQLiteDatabase
     * @param oldVersion reference to the old app version
     * @param newVersion reference to the new app version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // If we are upgrading to a new version drop the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Re-create the table
        onCreate(db);

    }


    ////////////// Custom Methods Start //////////////

    /**
     * getNumQuestions method
     * Grabs the numQuestions and returns it to the user
     * @return numQuestions integer
     */
    public int getNumQuestions() { return numQuestions; }

    /**
     * validateColumnCount method
     * Confirms that the number of columns corresponds to the number of questions, if not, create
     * a new table with the correct number of columns.
     */
    private void validateColumnCount() {

        //Create a new database
        SQLiteDatabase db = this.getReadableDatabase();

        if (getColumnCount(db) != (2 + numQuestions)) {

            // Print a warning, stating why we're dropping the table
            Log.w(TAG, "Dropping table due to question count change.");

            // Drop the table since the column count needs to change
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Recreate the table
            onCreate(db);

        }

        // Done reading from database so close the reference
        db.close();

    }

    /**
     * getColumnCount method
     * Gets and returns the number of columns in the database.
     * @param db SQLiteDatabase
     * @return columnCount integer
     */
    private int getColumnCount(SQLiteDatabase db) {

        // Create a query to get table info
        Cursor c = db.rawQuery("PRAGMA table_info(" + TABLE_NAME + ")", null);

        //Create a primitive and set it equal to the getColumnCount of c
        int columnCount = c.getColumnCount();

        // Done reading from cursor so make sure we close it
        c.close();

        //Return columnCount integer
        return columnCount;

    }

}