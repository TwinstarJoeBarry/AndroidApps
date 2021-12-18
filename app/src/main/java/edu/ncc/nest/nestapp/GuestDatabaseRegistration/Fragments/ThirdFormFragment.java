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
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.UIClasses.MultiSelectSpinner;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationThirdFormBinding;

/**
 * ThirdFormFragment: Represents a form that a guest can fill in with more of their information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link SecondFormFragment} and sends them to the next fragment (will be Fourth Fragment).
 */
public class ThirdFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationThirdFormBinding binding;

    // for testing
    MultiSelectSpinner multiselectDietary, multiselectEmployment, multiselectHealth, multiselectHousing;
    // instance variables for summary fragment
    private String dietary, employment, health, housing, snap, programs, income, otherProg;
    private Bundle result = new Bundle();

    // initialize backButtonCallback
    private OnBackPressedCallback backbuttonCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGuestDatabaseRegistrationThirdFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment (deprecated since bundle added 11.2021)
        //return inflater.inflate(R.layout.fragment_guest_database_registration_third_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // override back button to give a warning
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

        // target multiselect spinners on the layout
        multiselectDietary = binding.grf3Dietary;
        multiselectEmployment = binding.grf3StatusEmployment;
        multiselectHealth = binding.grf3StatusHealth;
        multiselectHousing = binding.grf3StatusHousing;
        // load them with items using the setItems() method in the MultiSelectSpinner class
        multiselectDietary.setItems(getResources().getStringArray(R.array.dietary_needs));
        multiselectEmployment.setItems(getResources().getStringArray(R.array.employment_status));
        multiselectHealth.setItems(getResources().getStringArray(R.array.health_status));
        multiselectHousing.setItems(getResources().getStringArray(R.array.housing_status));

        /*
        Button bt = binding.getSelected;
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = testSpinner.getSelectedItemsAsString();
                Log.e("getSelected", s);
            }
        });

         */


        // set onItemSelectedListener for dropdowns. Hardcoded. TODO Change to loop?
        // may need to update IDs .. thinking grf_3_input_dietary, etc. Then the textviews are
        // grf_3_textview_dietary. This way inputs are grouped and textviews are grouped.
        // hopefully then we can loop through them.
        //binding.grf3Dietary.setOnItemSelectedListener(dropdownListener);
        binding.grf3OtherProgs.setOnItemSelectedListener(dropdownListener);
        binding.grf3Snap.setOnItemSelectedListener(dropdownListener);
        //binding.grf3StatusEmployment.setOnItemSelectedListener(dropdownListener);
        //binding.grf3StatusHealth.setOnItemSelectedListener(dropdownListener);
        //binding.grf3StatusHousing.setOnItemSelectedListener(dropdownListener);

        // adds the onClick listener to the 'next' button
        binding.nextButtonThirdFragmentGRegistration.setOnClickListener(v -> {

            // store the selected items into the instance variables
            dietary = multiselectDietary.getSelectedItemsAsString();
            programs = binding.grf3OtherProgs.getSelectedItem().toString();
            snap = binding.grf3Snap.getSelectedItem().toString();
            employment = multiselectEmployment.getSelectedItemsAsString();
            health = multiselectHealth.getSelectedItemsAsString();
            housing = multiselectHousing.getSelectedItemsAsString();

            income = binding.grf3Income.getText().toString();

            // storing all values in bundle to send to summary fragment
            result.putString("dietary", dietary);
            result.putString("programs", programs);
            result.putString("snap", snap);
            result.putString("employment", employment);
            result.putString("health", health);
            result.putString("housing", housing);
            result.putString("income", income);

            // sending bundle
            getParentFragmentManager().setFragmentResult("sending_third_form_fragment_info", result);

            // navigate to the fourth fragment when clicked
            NavHostFragment.findNavController(ThirdFormFragment.this)
                    .navigate(R.id.action_DBR_ThirdFormFragment_to_FourthFormFragment);
        });

    }

    // This dropdown listener currently changes the first item in the spinner to muted text.
    // When a user selects an item other than the first, text changes to standard color.
    // Later, we can use this same logic for verification.
    // TODO can this be moved to a separate file and then just called?
    private final AdapterView.OnItemSelectedListener dropdownListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // if first item is selected, it's a placeholder. Treat as no input
            if(position == 0) {
                // Makes it look visually 'muted'
//                ((TextView) view).setTextColor(Color.GRAY);
            } else {
                // else, an item is selected. Below uses the "ColorPrimaryDark" variable. This will allow us to
                // keep universal themes and styling across the app.
                ((TextView) view).setTextColor(getResources().getColor(R.color.colorPrimaryDark, getContext().getTheme()));
                // Adds a visual UI response when selecting an item.
                Toast.makeText
                        (getContext(), "Selected : " + ((TextView) view).getText(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };



}