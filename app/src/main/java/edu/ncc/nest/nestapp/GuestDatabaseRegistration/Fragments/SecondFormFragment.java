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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFirstFormBinding;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSecondFormBinding;

/**
 * SecondFormFragment: Represents a form that a guest can fill in with their household information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link FirstFormFragment} and sends them to the next fragment {@link SecondFormFragment}.
 */
public class SecondFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationSecondFormBinding binding;

    private String inputStreetAddress1, inputStreetAddress2, inputCity, inputState, inputZip,
    inputAffiliation, inputAge, inputGender;

    private boolean validStreetAddress1, validCity, validZip, validInput = false;

    // back button override warning callback
    private OnBackPressedCallback backbuttonCallback;

    private String fname;
    private Bundle result = new Bundle();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if(savedInstanceState != null){
//            inputStreetAddress1 = savedInstanceState.getString("street address 1");
//            inputStreetAddress2 = savedInstanceState.getString("street address 2");
//            inputCity = savedInstanceState.getString("city");
//            inputState = savedInstanceState.getString("state");
//            inputZip = savedInstanceState.getString("zip");
//            inputAffiliation = savedInstanceState.getString("affiliation");
//            inputAge = savedInstanceState.getString("age");
//            inputGender = savedInstanceState.getString("gender");
//
//            binding.grf2Address1.setText(inputStreetAddress1);
//            binding.grf2Address2.setText(inputStreetAddress2);
//            binding.grf2City.setText(inputCity);
//            binding.grf2Zip.setText(inputZip);
//
//            binding.grf2State.getItemIdAtPosition(1);
//            binding.grf2Affiliation.getItemIdAtPosition(1);
//            binding.grf2Age.getItemIdAtPosition(1);
//            binding.grf2Gender.getItemIdAtPosition(1);
//        }
        // Inflate the layout for this fragment

        binding = FragmentGuestDatabaseRegistrationSecondFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Use Shared Preferences to store transitional data so data isn't lost when back button is pressed
        //For security we use private mode so only the app can access this info
        SharedPreferences sharedPref = getActivity().getSharedPreferences("BackFrag2", Context.MODE_PRIVATE);

        //Ovveride default back button behaviour
        backbuttonCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SharedPreferences.Editor editor = sharedPref.edit();

                //Clear Shared preferences to make sure
                editor.clear().commit();
                editor.putString("adr1", binding.grf2Address1.getText().toString());
                editor.putString("adr2", binding.grf2Address2.getText().toString());
                editor.putString("city", binding.grf2City.getText().toString());
                editor.putString("zip", binding.grf2Zip.getText().toString());
                editor.putInt("state", binding.grf2State.getSelectedItemPosition());
                editor.putInt("affiliation", binding.grf2Affiliation.getSelectedItemPosition());
                editor.putInt("age", binding.grf2Age.getSelectedItemPosition());
                editor.putInt("gender", binding.grf2Gender.getSelectedItemPosition());
                editor.commit();

                //set back button back to false then recall the back button invoking default behvio
                backbuttonCallback.setEnabled(false);
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backbuttonCallback);

/*
        TODO: Remove this when back instance state is tested successfully
        This was to give a warning that when back button is pressed data will not be saved.
        This is no longer necessary because data is now saved on back press.
        
        backbuttonCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // show dialog prompting user
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("Are you sure?");
                builder.setMessage("Data entered on this page may not be saved.");
                // used to handle the 'confirm' button
                builder.setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // continue by disabling this callback then calling the backpressedispatcher again
                        // when this was enabled, it was at top of backpressed stack. When we disable, the next item is default back behavior
                        backbuttonCallback.setEnabled(false);
                        requireActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                });
                // handles the 'cancel' button
                builder.setNegativeButton("Stay On This Page", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // tells android we 'canceled', not dismiss. more appropriate
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        };
        // need to add the callback to the activities backpresseddispatcher stack.
        // if enabled, it will run this first. If disabled, it will run the default (next item in stack)
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backbuttonCallback);
*/

        //Blocked out back button ovveride is above

        //Toast.makeText(getContext(), "WARNING: Pressing back will clear data. Please double check before continuing.", Toast.LENGTH_LONG).show();

        /*
        getParentFragmentManager().setFragmentResultListener("sending_first_form_fragment_info", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        binding.welcomeMessageName.setText(result.getString("First Name"));
                    }
                });

         */

        //Use shared preferences to write possible state data back after back button is pressed
        binding.grf2Address1.setText(sharedPref.getString("adr1", ""));
        binding.grf2Address2.setText(sharedPref.getString("adr2", ""));
        binding.grf2City.setText(sharedPref.getString("city", ""));
        binding.grf2Zip.setText(sharedPref.getString("zip", ""));
        binding.grf2State.setSelection(sharedPref.getInt("state", 1));
        binding.grf2Affiliation.setSelection(sharedPref.getInt("affiliation", 1));
        binding.grf2Age.setSelection(sharedPref.getInt("age", 1));
        binding.grf2Gender.setSelection(sharedPref.getInt("gender", 1));

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputStreetAddress1 = ((EditText) getView().findViewById(R.id.grf_2_address1)).getText().toString();
                        inputStreetAddress2 = ((EditText) getView().findViewById(R.id.grf_2_address2)).getText().toString();
                        inputCity = ((EditText) getView().findViewById(R.id.grf_2_city)).getText().toString();
                        inputState = ((Spinner) getView().findViewById(R.id.grf_2_state)).getSelectedItem().toString();
                        inputZip = ((EditText) getView().findViewById(R.id.grf_2_zip)).getText().toString();
                        inputAffiliation = ((Spinner) getView().findViewById(R.id.grf_2_affiliation)).getSelectedItem().toString();
                        inputAge = ((Spinner) getView().findViewById(R.id.grf_2_age)).getSelectedItem().toString();
                        inputGender = ((Spinner) getView().findViewById(R.id.grf_2_gender)).getSelectedItem().toString();

                        if(inputStreetAddress1.length() == 0){
                            binding.streetAddress1Textview.setTextColor(Color.RED);
                            binding.streetAddress1Textview.setText("You must enter an address");
                            validStreetAddress1 = false;
                        }
                        else{
                            binding.streetAddress1Textview.setTextColor(Color.GRAY);
                            binding.streetAddress1Textview.setText("Enter your street address");
                            result.putString("Street Address 1", inputStreetAddress1);
                            validStreetAddress1 = true;
                        }

                        if(inputStreetAddress2.length() != 0){
                            result.putString("Street Address 2", inputStreetAddress2);
                        }

                        if(inputCity.length() == 0){
                            binding.cityTextview.setTextColor(Color.RED);
                            binding.cityTextview.setText("You must enter the city you live in");
                            validCity = false;
                        }
                        else{
                            binding.cityTextview.setTextColor(Color.GRAY);
                            binding.cityTextview.setText("Enter your city");
                            result.putString("City", inputCity);
                            validCity = true;
                        }

                        result.putString("State", inputState);

                        if(inputZip.length() == 0){
                            binding.zipTextview.setTextColor(Color.RED);
                            binding.zipTextview.setText("You must enter your zip code");
                            validZip = false;
                        }
                        else{
                            binding.zipTextview.setTextColor(Color.GRAY);
                            binding.zipTextview.setText("Enter your zip code");
                            result.putString("Zip", inputZip);
                            validZip = true;
                        }

                        result.putString("Affiliation", inputAffiliation);

                        result.putString("Age", inputAge);

                        result.putString("Gender", inputGender);

                        validInput = validStreetAddress1 & validCity & validZip;

                        if(validInput){
                            sharedPref.edit().clear().commit();
                            getParentFragmentManager().setFragmentResult("sending_second_form_fragment_info", result);
                            NavHostFragment.findNavController(SecondFormFragment.this)
                                    .navigate(R.id.action_DBR_SecondFormFragment_to_DBR_ThirdFormFragment);
                        }
                    }
                });

    }
    }