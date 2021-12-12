package edu.ncc.nest.nestapp.GuestVisit.DatabaseClasses;

/**
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
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

/**
 * QuestionnaireSource: This class finds and inserts data from and to the
 * {@link QuestionnaireHelper} class / database.
 */
public class QuestionnaireSource {

    public static final String TAG = QuestionnaireSource.class.getSimpleName();

    private final QuestionnaireHelper QUESTIONNAIRE_HELPER;
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    private String[] counterStr = {QuestionnaireHelper.VISIT_COUTNER};


    /************ Constructor ************/

    public QuestionnaireSource(Context context) {
        QUESTIONNAIRE_HELPER = new QuestionnaireHelper(context);
    }

    /************ Custom Methods Start ************/

    /**
     * open --
     * Opens the database
     * @return Returns a reference to this QuestionnaireSource object
     */
    public QuestionnaireSource open() throws SQLException {

        writableDatabase = QUESTIONNAIRE_HELPER.getWritableDatabase();

        readableDatabase = QUESTIONNAIRE_HELPER.getReadableDatabase();

        return this;

    }

    /**
     * close --
     * Closes the database
     */
    public void close() {

        writableDatabase.close();

        readableDatabase.close();

    }

    public void clearData() {
        writableDatabase.delete(QuestionnaireHelper.TABLE_NAME, null, null);
        readableDatabase.delete(QuestionnaireHelper.TABLE_NAME, null, null);
    }

    /**
     * submitQuestionnaire --
     * Submits answers by guest (guestID) into the database
     * @param guestID The ID of the guest the submission belongs to
     * @return Whether or not there was an error submitting the questionnaire
     */
    public long submitQuestionnaire(@NonNull String guestID, @NonNull String adultCount, @NonNull String seniorCount,
                                    @NonNull String childCount, @NonNull String firstVisit) {

        ContentValues submissionValues = new ContentValues();

        //Setup the data and time for when the Questionnaire was submitted
        DateTimeFormatter frmt = DateTimeFormatter.ofPattern("MMddyyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = frmt.format(now);

        // Put the guest's id into cValues
        submissionValues.put(QuestionnaireHelper.GUEST_ID, guestID);
        submissionValues.put(QuestionnaireHelper.ADULT_COUNT, adultCount);
        submissionValues.put(QuestionnaireHelper.SENIOR_COUNT, seniorCount);
        submissionValues.put(QuestionnaireHelper.CHILD_COUNT, childCount);
        submissionValues.put(QuestionnaireHelper.FIRST_VISIT, firstVisit);
        submissionValues.put(QuestionnaireHelper.DATE, currentTime);
        submissionValues.put(QuestionnaireHelper.VISIT_COUTNER, "0");

        // Insert the submission in the database and return the row id it was stored at
        return (writableDatabase.insert(QuestionnaireHelper.TABLE_NAME, null, submissionValues));

    }



    /**
     * findSubmissions --
     * Finds submissions by a guest ( guestID ) and adds
     * them to a list of submissions.
     * @param guestID The ID to search for
     * @return The list of submissions
     */
    /*
    public List<QuestionnaireSubmission> findSubmissions(@NonNull String guestID) {

        List<QuestionnaireSubmission> submissions = new ArrayList<>();

        // Get all the guest records from the table (TABLE_NAME) who's field name (GUEST_ID) matches the field value (?).
        String sqlQuery = "SELECT * FROM " + QuestionnaireHelper.TABLE_NAME +
                " WHERE " + QuestionnaireHelper.GUEST_ID + " = ?";

        // Run the SQL query from above and replace the '?' character with the respective argument stored in the String[] array
        Cursor cursor = readableDatabase.rawQuery(sqlQuery, new String[] {guestID});

        // Loop through each cursor, convert them to submissions, and add it to the list
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())

            submissions.add(convertCursorToSubmission(cursor));

        // Make sure we close the cursor since we don't need it anymore
        cursor.close();

        return submissions;

    }

    */

    /*
    /**
     * printSubmissions --
     * Searches through the database for questionnaires
     * submitted by a guest ( guestID ). Prints each
     * questionnaire submitted by the guest.
     * @param guestID The ID of the guest we're getting the submissions of
     */
    public void printSubmissions(@NonNull String guestID) {

        Log.i(TAG, "Printing all submissions by guest " + guestID + " from " + QuestionnaireHelper.DATABASE_NAME + ":");

        // Find each questionnaire submitted by the guest and print it to the log

    }
    /*
    /**
     * convertCursorToSubmission --
     * Converts a Cursor object to a QuestionnaireSubmission object.
     * @param c The Cursor to convert
     * @return Returns the result of the conversion
     */

    /*
    private QuestionnaireSubmission convertCursorToSubmission(Cursor c) {
        ArrayList<String> guestAnswers = new ArrayList<>(4);

        // Retrieve each answer from the Cursor
        for (int i = 0; i < guestAnswers.size(); i++)

            guestAnswers.add(c.getString(2 + i));

        // 0 - ROW_ID, 1 - GUEST_ID
        return new QuestionnaireSubmission(c.getLong(0),
                c.getString(1), guestAnswers);
    }

     */

    public String getVisitCount(String barcode){
        QuestionnaireSubmission entry;
        Cursor cursor = readableDatabase.query(QuestionnaireHelper.TABLE_NAME,
                counterStr,
                QuestionnaireHelper.GUEST_ID + " = '" + barcode + "' ",
                null,
                null,
                null,
                QuestionnaireHelper.VISIT_COUTNER + " DESC ");

        cursor.moveToFirst();


        return "";

    }

}
