package edu.ncc.nest.nestapp.FragmentsGuestVisit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
        inputFields = getInputFields((ViewGroup) view);

        // Get a reference to the submit button and set this class as the onClickListener
        ((Button) view.findViewById(R.id.questionnaire_submit_btn)).setOnClickListener(this);

        if (savedInstanceState == null) {

            // NOTE: The following code may be only temporary depending on what defines the Guest's Id

            // Get info passed from the fragment result
            getParentFragmentManager().setFragmentResultListener("CONFIRM_SCAN",
                    this, (requestKey, result) -> {

                        final String BARCODE = result.getString("BARCODE");

                        if (BARCODE != null) {

                            EditText guestId = (EditText) inputFields.get(0);

                            // Set the input field related to the Guest's Id as the barcode we scanned in previous fragment
                            guestId.setText(BARCODE);

                            // Disable it so the user can't modify his check-in id.
                            guestId.setEnabled(false);

                        }

                    });

        } else {

            // This line may only be temporary depending on what defines the Guest's Id
            inputFields.get(0).setEnabled(savedInstanceState.getBoolean("GUEST_ID_ENABLED"));

            String[] fieldText = savedInstanceState.getStringArray("INPUT_FIELD_TEXT");

            for (int i = fieldText.length; i-- > 0;) {

                View inputField = inputFields.get(i);

                if (inputField instanceof EditText)

                    ((EditText) inputField).setText(fieldText[i]);

                else if (inputField instanceof Spinner)

                    ((Spinner) inputField).setSelection(fieldText[i].equals("Yes") ? 0 : 1);

            }

        }

    }

    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // These next two lines may only be temporary depending on what defines the Guest's Id
        boolean guestIdEnabled = inputFields.get(0).isEnabled();

        outState.putBoolean("GUEST_ID_ENABLED", guestIdEnabled);

        String[] fieldText = new String[inputFields.size()];

        for (int i = fieldText.length; i-- > 0;)

            fieldText[i] = getFieldText(inputFields.get(i));

        outState.putStringArray("INPUT_FIELD_TEXT", fieldText);

        super.onSaveInstanceState(outState);

    }

    ////////////// Implementation Methods Start  //////////////

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.questionnaire_submit_btn) {

            List<String> fieldTexts = new ArrayList<>();

            for (View inputField : inputFields) {

                // Get the text from the current input field
                String fieldText = getFieldText(inputField);

                // If the field is empty set its value in the list to null
                fieldTexts.add(!fieldText.isEmpty() ? fieldText : null);

            }

            Log.d(TAG, "Guest's Answers: " + fieldTexts.toString());

            // Create an Intent that will bring the user back to the home page
            Intent intent = new Intent(requireContext(), Choose.class);

            // Clears the activity stack when using the intent
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Go to the home page
            startActivity(intent);

            // Make sure we finish() our underlying activity
            requireActivity().finish();

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
                    idList.addAll(getInputFields((ViewGroup) view));

                if (view instanceof EditText || view instanceof Spinner)

                    idList.add(view);

            }

        }

        return idList;

    }

    /**
     * Takes 1 NonNull parameter. Checks whether or not the supplied view is an instance of
     * EditText or Spinner then casts to it. It then returns the string entered by the user that
     * is stored within that view.
     * @param view The view to get the text from
     * @return The string entered into that view by the user
     */
    private static String getFieldText(@NonNull View view) {

        if (view instanceof EditText)

            return ((EditText) view).getText().toString();

        else if (view instanceof Spinner)

            return ((Spinner) view).getSelectedItem().toString();

        throw new ClassCastException("Parameter view is not a valid input field");

    }

}
