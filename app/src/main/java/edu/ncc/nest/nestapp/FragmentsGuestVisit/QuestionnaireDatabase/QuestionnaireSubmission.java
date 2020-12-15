package edu.ncc.nest.nestapp.FragmentsGuestVisit.QuestionnaireDatabase;

import java.util.ArrayList;

public class QuestionnaireSubmission {

    private final ArrayList<String> GUEST_ANSWERS;
    public final String GUEST_ID;
    public final long ROW_ID;

    public QuestionnaireSubmission(long rowID, String guestID, ArrayList<String> guestAnswers) {

        GUEST_ANSWERS = guestAnswers;

        GUEST_ID = guestID;

        ROW_ID = rowID;

    }

}
