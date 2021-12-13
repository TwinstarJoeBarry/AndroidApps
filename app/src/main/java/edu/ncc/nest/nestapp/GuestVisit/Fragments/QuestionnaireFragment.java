package edu.ncc.nest.nestapp.GuestVisit.Fragments;

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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.ncc.nest.nestapp.GuestVisit.DatabaseClasses.QuestionnaireSource;
import edu.ncc.nest.nestapp.R;

/**
 * QuestionnaireFragment: Used to ask a guest a set of questions about their visit.
 */
public class QuestionnaireFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = QuestionnaireFragment.class.getSimpleName();

    // Stores a list of all the views that contain the user's responses
    private List<View> inputFields;

    private Button submitButton;

    private String guestID;

    QuestionnaireSource datasource;


    ////////////// Lifecycle Methods Start //////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_visit_questionnaire, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get all of the input fields from the view
        inputFields = getInputFields(view);

        // Get a reference to the submit button and set this class as the onClickListener
        submitButton = view.findViewById(R.id.questionnaire_submit_btn);

        submitButton.setOnClickListener(this);

        if (savedInstanceState != null) {

            guestID = savedInstanceState.getString("GUEST_ID");

            submitButton.setEnabled(savedInstanceState.getBoolean("SUBMIT_ENABLED"));

            boolean[] isEnabled = savedInstanceState.getBooleanArray("INPUT_FIELD_ENABLED");

            if (isEnabled != null) {

                for (int i = isEnabled.length; i-- > 0; )

                    inputFields.get(i).setEnabled(isEnabled[i]);

            } else

                throw new NullPointerException("Failed to restore saved state");

        } else

            // Get info passed from the fragment result
            getParentFragmentManager().setFragmentResultListener("GUEST_CONFIRMED",
                    this, (requestKey, result) -> {

                        if ((guestID = result.getString("GUEST_ID")) != null) {

                            // Get the EditText of the first question
                            EditText guestIDInputField = (EditText) inputFields.get(0);

                            // Set the input field's text related to the Guest's Id as the barcode we scanned in previous fragment
                            guestIDInputField.setText(guestID);

                            // Disable it so the user can't modify his check-in id.
                            guestIDInputField.setEnabled(false);

                        } else

                            Log.e(TAG, "ERROR: Failed to retrieve GUEST_ID.");

                    });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        boolean[] isEnabled = new boolean[inputFields.size()];

        // Loop through each input field and get the enabled state of each field
        for (int i = isEnabled.length; i-- > 0;)

            isEnabled[i] = inputFields.get(i).isEnabled();

        // Put the all field enabled states into the Bundle
        outState.putBooleanArray("INPUT_FIELD_ENABLED", isEnabled);

        // Put the enabled state of the submit button into the bundle
        outState.putBoolean("SUBMIT_ENABLED", submitButton.isEnabled());

        outState.putString("GUEST_ID", guestID);

        super.onSaveInstanceState(outState);

    }

    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.questionnaire_submit_btn) {

            // Used to store the guest's answers
            List<String> fieldTexts = new ArrayList<>();

            for (View inputField : inputFields) {

                // Get the text from the current input field
                String fieldText = getInputFieldText(inputField);

                if (fieldText.isEmpty()) {

                    // Create a Builder for an AlertDialog
                    Builder alertDialogBuilder = new Builder(requireContext());

                    // Build the AlertDialog
                    alertDialogBuilder.setTitle(R.string.questionnaire_fragment_alert_title);

                    alertDialogBuilder.setMessage(R.string.questionnaire_fragment_alert_msg);

                    alertDialogBuilder.setPositiveButton("OK", null);

                    alertDialogBuilder.setCancelable(false);

                    // Create the AlertDialog and show it
                    alertDialogBuilder.create().show();

                    return;

                } else

                    // If the field is empty set its value in the list to null
                    fieldTexts.add(fieldText);

            }

            // If we make it here than the questionnaire is OK to submit

            // Disable all the input fields and submit button so the guest can review their answers

            for (View inputField : inputFields)

                inputField.setEnabled(false);

            submitButton.setEnabled(false);

            // Store answers into the Questionnaire database

            // Open a database
            QuestionnaireSource db = new QuestionnaireSource(requireContext(), inputFields.size()).open();

            // Submit the questionnaire into the database
            long rowID = db.submitQuestionnaire(guestID, fieldTexts);

            // If there wasn't any errors submitting the database
            if (rowID != -1) {

                // Print the answers to the log and display a toast

                Log.d(TAG, "Questionnaire Submitted: {Row ID: [" + rowID +
                        "], Guest ID: [" + guestID + "], Submission: " + fieldTexts.toString() + "}");

                Toast.makeText(getContext(), "Questionnaire Submitted. See Log.", Toast.LENGTH_SHORT).show();

                // TODO Delete this or comment it out as this is only temporary and for debugging purposes
                // NOTE: The list may grow extremely long in some cases and may clutter the log.
                // Print a list of all submissions by this guest from the database
                db.printSubmissions(guestID);

            } else

                Log.e(TAG, "ERROR: Failed to submit questionnaire.");

            // Finally make sure we close the database since it is no longer needed
            //db.close();

        }

    }


    ////////////// Custom Methods Start  //////////////

    /**
     * Takes 1 NonNull parameter. Gets and returns all of the input field views from the view
     * supplied in a list. It will look through the whole view and get each EditText and Spinner
     * in their respective order.
     * @param view The root view to get the input fields from
     * @return A list containing all of the input fields it found
     */
    private static List<View> getInputFields(@NonNull View view) {

        List<View> idList = new ArrayList<>();

        if (view instanceof ViewGroup) {

            // Cast the root view to a ViewGroup
            ViewGroup viewGroup = (ViewGroup) view;

            // Loop through each child of the view
            for (int i = 0, c = viewGroup.getChildCount(); i < c; i++) {

                if ((view = viewGroup.getChildAt(i)) instanceof ViewGroup)

                    // Add all the children of the child ViewGroup to the list
                    idList.addAll(getInputFields(view));

                if (view instanceof EditText || view instanceof Spinner)

                    idList.add(view);

            }

        }

        return idList;

    }

    /**
     * Takes 1 NonNull parameter. Checks whether or not the supplied View is an instance of
     * EditText or Spinner then casts to it. It then returns the string entered by the user, that
     * is stored within that view.
     * @param view The View/field to get the text from
     * @return The string entered into that View/field by the user
     * @throws ClassCastException If the View/field is not an instance of EditText or Spinner
     */
    private static String getInputFieldText(@NonNull View view) {

        if (view instanceof EditText)

            return ((EditText) view).getText().toString();

        else if (view instanceof Spinner)

            return ((Spinner) view).getSelectedItem().toString();

        throw new ClassCastException("Parameter view is not a valid input field");

    }

}
