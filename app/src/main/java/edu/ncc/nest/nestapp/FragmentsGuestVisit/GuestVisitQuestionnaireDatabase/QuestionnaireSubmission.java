package edu.ncc.nest.nestapp.FragmentsGuestVisit.QuestionnaireDatabase;

import java.util.ArrayList;

public class QuestionnaireSubmission {

    private final ArrayList<String> GUEST_ANSWERS;
    public final String GUEST_ID;
    public final long ROW_ID;


    ////////////// Constructor //////////////

    public QuestionnaireSubmission(long rowID, String guestID, ArrayList<String> guestAnswers) {

        GUEST_ANSWERS = guestAnswers;

        GUEST_ID = guestID;

        ROW_ID = rowID;

    }


    ////////////// Other Class Methods //////////////

    @Override
    public boolean equals(Object other) {

        if (other instanceof QuestionnaireSubmission)

            return (this.ROW_ID == ((QuestionnaireSubmission) other).ROW_ID);

        return false;

    }

    @Override
    public String toString() {

        return ("{ Row: " + ROW_ID + ", Guest ID: " + GUEST_ID + ", Answers: " + GUEST_ANSWERS.toString() + " }");

    }

}
