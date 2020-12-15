package edu.ncc.nest.nestapp.FragmentsGuestVisit;

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

import edu.ncc.nest.nestapp.R;

/**
 * This fragment is used to ask a guest a set of questions about their visit.
 */
public class GuestQuestionnaireFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GuestQuestionnaireFragment.class.getSimpleName();

    // Stores a list of all the views that contain the user's responses
    private List<View> inputFields;

    private Button submitButton;


    ////////////// Lifecycle Methods Start //////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_questionnaire, container, false);

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

                        final String GUEST_ID = result.getString("GUEST_ID");

                        if (GUEST_ID != null) {

                            // Get the EditText of the first question
                            EditText guestId = (EditText) inputFields.get(0);

                            // Set the input field's text related to the Guest's Id as the barcode we scanned in previous fragment
                            guestId.setText(GUEST_ID);

                            // Disable it so the user can't modify his check-in id.
                            guestId.setEnabled(false);

                        }

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
                    alertDialogBuilder.setTitle(R.string.questionnaire_alert_title);

                    alertDialogBuilder.setMessage(R.string.questionnaire_alert_msg);

                    alertDialogBuilder.setPositiveButton("OK", null);

                    alertDialogBuilder.setCancelable(false);

                    // Create the AlertDialog and show it
                    alertDialogBuilder.create().show();

                    return;

                } else

                    // If the field is empty set its value in the list to null
                    fieldTexts.add(fieldText);

            }

            for (View inputField : inputFields)

                inputField.setEnabled(false);

            // Disable the submit button
            submitButton.setEnabled(false);

            // Print the answers to the log
            Log.d(TAG, "Guest's Answers: " + fieldTexts.toString());

            // Display toast saying the questionnaire has been submitted
            Toast.makeText(getContext(), "Questionnaire Submitted. See Log.", Toast.LENGTH_SHORT).show();

            // TODO Store the answers in a local database



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
