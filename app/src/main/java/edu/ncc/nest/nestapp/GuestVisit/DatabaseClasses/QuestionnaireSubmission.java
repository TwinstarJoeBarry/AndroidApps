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

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * QuestionnaireSubmission Class
 *
 * Represents a submission in the questionnaire database.
 *
 * @author Tyler Sizse
 * @author (Co-Author) Owen Wurster
 * @author (Co-Author) Charles Cohen
 */
public class QuestionnaireSubmission {

    public static final String TAG = QuestionnaireSubmission.class.getSimpleName();

    private final ArrayList<String> GUEST_ANSWERS;
    public final String GUEST_ID;
    public final long ROW_ID;


    ////////////// Constructor //////////////

    /**
     * QuestionnaireSubmission --
     * Constructor for QuestionnaireSubmission class
     * @param rowID The ID of the row this submission is located at in the database
     * @param guestID The ID of the guest related to this submission
     * @param guestAnswers The answers stored related to this submission
     */
    public QuestionnaireSubmission(long rowID, String guestID, ArrayList<String> guestAnswers) {

        GUEST_ANSWERS = guestAnswers;

        GUEST_ID = guestID;

        ROW_ID = rowID;

    }


    ////////////// Other Class Methods //////////////

    /**
     * equals --
     * Compares this object with another and returns whether or not it is the same object.
     * @param other The other object to compare to
     * @return Returns whether or not 'other' is the same Object as this one
     */
    @Override
    public boolean equals(Object other) {

        if (other instanceof QuestionnaireSubmission)

            return (this.ROW_ID == ((QuestionnaireSubmission) other).ROW_ID);

        return false;

    }

    /**
     * toString --
     * Returns a string that represents this classes contents.
     */
    @NonNull
    @Override
    public String toString() {

        return ("{Row ID: [" + ROW_ID + "], Guest ID: [" + GUEST_ID + "], Submission: " + GUEST_ANSWERS.toString() + "}");

    }

}
