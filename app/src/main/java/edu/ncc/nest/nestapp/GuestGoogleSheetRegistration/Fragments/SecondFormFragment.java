package edu.ncc.nest.nestapp.GuestGoogleSheetRegistration.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.ncc.nest.nestapp.R;

/**
 *
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

public class SecondFormFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = SecondFormFragment.class.getSimpleName();

    //variables to store user information
    private EditText people, income, snap, otherPrograms, employmentStatus, healthStatus,
            housingStatus, childcareStatus, childrenUnderOne, childrenBetweenOneAndFive,
            childrenBetweenSixAndTwelve, childrenBetweenThirteenAndEighteen, dietary,
            howDidYouFind, moreInfoQuestion, volFirstName, volLastName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"In SecondFormFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_google_sheet_registration_second_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting a handle on info from the UI
        people = (EditText) view.findViewById(R.id.editText);
        income = (EditText) view.findViewById(R.id.editText2);
        snap = (EditText) view.findViewById(R.id.editText3);
        otherPrograms = (EditText) view.findViewById(R.id.editText4);
        employmentStatus = (EditText) view.findViewById(R.id.editText5);
        healthStatus = (EditText) view.findViewById(R.id.editText6);
        housingStatus = (EditText) view.findViewById(R.id.editText7);
        childcareStatus = (EditText) view.findViewById(R.id.editText8);
        childrenUnderOne = (EditText) view.findViewById(R.id.editText9);
        childrenBetweenOneAndFive = (EditText) view.findViewById(R.id.editText10);
        childrenBetweenSixAndTwelve = (EditText) view.findViewById(R.id.editText11);
        childrenBetweenThirteenAndEighteen = (EditText) view.findViewById(R.id.editText12);
        dietary = (EditText) view.findViewById(R.id.editText13);
        howDidYouFind = (EditText) view.findViewById(R.id.editText14);
        moreInfoQuestion = (EditText) view.findViewById(R.id.editText15);
        volFirstName = (EditText) view.findViewById(R.id.editText16);
        volLastName = (EditText) view.findViewById(R.id.editText17);

        /*
        getParentFragmentManager().setFragmentResultListener("SEND_MESSAGE", this, new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {

                //String message = bundle.getString("MESSAGE");

                //((TextView) getView().findViewById(R.id.editText2)).setText(message);

            }

        });*/

        view.findViewById(R.id.submit_button).setOnClickListener(view1 -> {

            /* NavHostFragment.findNavController(SecondFormFragment.this)
                    .navigate(R.id.action_GSR_SecondFormFragment_to_FirstFormFragment);*/

        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG,"In SecondFormFragment onStart()");

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
