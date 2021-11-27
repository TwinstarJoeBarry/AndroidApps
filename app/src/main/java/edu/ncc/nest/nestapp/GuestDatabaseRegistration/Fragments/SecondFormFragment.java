package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSecondFormBinding;

/**
 * SecondFormFragment: Represents a form that a guest can fill in with their household information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link FirstFormFragment} and sends them to the next fragment {@link SummaryFragment}.
 */
public class SecondFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationSecondFormBinding binding;

    private String inputStreetAddress1, inputStreetAddress2, inputCity, inputState, inputZip,
    inputAffiliation, inputAge, inputGender;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_second_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        getParentFragmentManager().setFragmentResultListener("sending_first_form_fragment_info", this,
//                new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
//
//                        String firstName = result.getString("First Name");
////                        binding.welcomeMessageName.setText(firstName);
//                    }
//                });

//        binding.nextButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//
//                inputStreetAddress1 = ((EditText) getView().findViewById(R.id.guestreg_address1)).getText().toString();
//                inputStreetAddress2 = ((EditText) getView().findViewById(R.id.guestreg_address2)).getText().toString();
//                inputCity = ((EditText) getView().findViewById(R.id.guestreg_city)).getText().toString();
////                inputState = ((EditText) getView().findViewById(R.id.guestreg_state)).getText().toString();
//                inputZip = ((EditText) getView().findViewById(R.id.guestreg_zip)).getText().toString();
//                inputAffiliation = ((EditText) getView().findViewById(R.id.guestreg_affiliation)).getText().toString();
//                inputAge = ((EditText) getView().findViewById(R.id.guestreg_age)).getText().toString();
////                inputGender = ((EditText) getView().findViewById(R.id.guestreg_gender)).getText().toString();
//
//
//            }
//        });
    }

}