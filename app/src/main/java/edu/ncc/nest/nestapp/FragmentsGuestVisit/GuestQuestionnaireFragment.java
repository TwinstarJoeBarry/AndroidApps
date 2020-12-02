package edu.ncc.nest.nestapp.FragmentsGuestVisit;

import android.os.Bundle;
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

import edu.ncc.nest.nestapp.R;

public class GuestQuestionnaireFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = GuestQuestionnaireFragment.class.getSimpleName();

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

        inputFields = getInputFields((ViewGroup) view);

        submitButton = ((Button) view.findViewById(R.id.questionnaire_submit_btn));

        submitButton.setOnClickListener(this);

    }

    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onClick(View v) {

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

        ViewGroup viewGroup = (ViewGroup) view;

        List<View> idList = new ArrayList<>();

        for (int i = 0, c = viewGroup.getChildCount(); i < c; i++) {

            if ((view = viewGroup.getChildAt(i)) instanceof ViewGroup)

                idList.addAll(getInputFields((ViewGroup) view));

            if (view instanceof EditText || view instanceof Spinner)

                idList.add(view);

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
