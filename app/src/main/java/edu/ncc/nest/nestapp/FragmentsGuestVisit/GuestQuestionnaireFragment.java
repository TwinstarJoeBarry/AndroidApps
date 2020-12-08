package edu.ncc.nest.nestapp.FragmentsGuestVisit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.R;

/**
 * This fragment is used to ask a guest a set of questions about their visit.
 */
public class GuestQuestionnaireFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GuestQuestionnaireFragment.class.getSimpleName();

    // Stores a list of all the views that contain the user's responses
    private List<View> inputFields;


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
        view.findViewById(R.id.questionnaire_submit_btn).setOnClickListener(this);

        if (savedInstanceState != null) {

            boolean[] isEnabled = savedInstanceState.getBooleanArray("INPUT_FIELD_IS_ENABLED");

            if (isEnabled != null) {

                for (int i = isEnabled.length; i-- > 0; )

                    inputFields.get(i).setEnabled(isEnabled[i]);

            } else

                throw new NullPointerException("Failed to restore saved state");

        } else

            // NOTE: The following code may be only temporary depending on what defines the Guest's Id
            // Get info passed from the fragment result
            getParentFragmentManager().setFragmentResultListener("GUEST_CONFIRMED",
                    this, (requestKey, result) -> {

                        final String BARCODE = result.getString("GUEST_ID");

                        if (BARCODE != null) {

                            EditText guestId = (EditText) inputFields.get(0);

                            // Set the input field related to the Guest's Id as the barcode we scanned in previous fragment
                            guestId.setText(BARCODE);

                            // Disable it so the user can't modify his check-in id.
                            guestId.setEnabled(false);

                        }

                    });

    }


    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        boolean[] isEnabled = new boolean[inputFields.size()];

        // Loop through each input field and get the enabled state of each field
        for (int i = isEnabled.length; i-- > 0;)

            isEnabled[i] = inputFields.get(i).isEnabled();

        // Put the all field enabled states into the Bundle
        outState.putBooleanArray("INPUT_FIELD_IS_ENABLED", isEnabled);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.questionnaire_submit_btn) {

            List<String> fieldTexts = new ArrayList<>();

            for (View inputField : inputFields) {

                // Get the text from the current input field
                String fieldText = getInputFieldText(inputField);

                if (fieldText.isEmpty()) {

                    // Create a Builder for an AlertDialog
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());

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

            Log.d(TAG, "Guest's Answers: " + fieldTexts.toString());

            Toast.makeText(getContext(), "Questionnaire Submitted. See Log.", Toast.LENGTH_SHORT).show();

            // Disabled this for now for test so we can compare the log statement to the answers entered.

            /*// Create an Intent that will bring the user back to the home page
            Intent intent = new Intent(requireContext(), Choose.class);

            // Clears the activity stack when using the intent
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Go to the home page
            startActivity(intent);

            // Make sure we finish() our underlying activity
            requireActivity().finish();*/

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
