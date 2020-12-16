package edu.ncc.nest.nestapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

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

public class GuestFormSecondFragment extends Fragment {
    protected static final String TAG = "TESTING";
    //variables to store user information
    EditText people, income, snap, otherPrograms, employmentStatus, healthStatus,
            housingStatus, childcareStatus, childrenUnderOne, childrenBetweenOneAndFive,
            childrenBetweenSixAndTwelve, childrenBetweenThirteenAndEighteen, dietary,
            howDidYouFind, moreInfoQuestion, volFirstName, volLastName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG,"In SecondFragment onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_guest_form_second_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        people = (EditText)(getView().findViewById(R.id.editText));
        income = (EditText)(getView().findViewById(R.id.editText2));
        snap = (EditText)(getView().findViewById(R.id.editText3));
        otherPrograms = (EditText)(getView().findViewById(R.id.editText4));
        employmentStatus = (EditText)(getView().findViewById(R.id.editText5));
        healthStatus = (EditText)(getView().findViewById(R.id.editText6));
        housingStatus = (EditText)(getView().findViewById(R.id.editText7));
        childcareStatus = (EditText)(getView().findViewById(R.id.editText8));
        childrenUnderOne = (EditText)(getView().findViewById(R.id.editText9));
        childrenBetweenOneAndFive = (EditText)(getView().findViewById(R.id.editText10));
        childrenBetweenSixAndTwelve = (EditText)(getView().findViewById(R.id.editText11));
        childrenBetweenThirteenAndEighteen = (EditText)(getView().findViewById(R.id.editText12));
        dietary = (EditText)(getView().findViewById(R.id.editText13));
        howDidYouFind = (EditText)(getView().findViewById(R.id.editText14));
        moreInfoQuestion = (EditText)(getView().findViewById(R.id.editText15));
        volFirstName = (EditText)(getView().findViewById(R.id.editText16));
        volLastName = (EditText)(getView().findViewById(R.id.editText17));;

        getParentFragmentManager().setFragmentResultListener("SEND_MESSAGE", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                //String message = bundle.getString("MESSAGE");
                //((TextView)getView().findViewById(R.id.editText2)).setText(message);
            }
        });

        view.findViewById(R.id.prev_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(GuestFormSecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}
