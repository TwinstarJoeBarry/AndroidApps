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
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFourthFormBinding;
//import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSecondFormBinding;

/**
 * ThirdFormFragment: Represents a form that a guest can fill in with more of their information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link ThirdFormFragment} and sends them to the next fragment {@link SummaryFragment}.
 */
public class FourthFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationFourthFormBinding binding;

    private String householdNum, childcareStatus, children1, children5,
        children12, children18;

    private Spinner numPeopleSpinner, childcareSpinner, children1Spinner,
            children5Spinner, children12Spinner, children18Spinner;
    

    // flags for hiding/showing views
    private boolean openView1, openView2, openView3, openView4, openView5 = false;

    private Bundle result = new Bundle();

    private OnBackPressedCallback backbuttonCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // implement binding to inflate layout
        binding = FragmentGuestDatabaseRegistrationFourthFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment (deprecated since bundle added 11.2021)
        //return inflater.inflate(R.layout.fragment_guest_database_registration_fourth_form, container, false);

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

        // store the bindings so its cleaner
        numPeopleSpinner  = binding.grf4NumPeople;
        childcareSpinner  = binding.grf4StatusChildcare;
        children1Spinner  = binding.grf4Children1;
        children5Spinner  = binding.grf4Children5;
        children12Spinner = binding.grf4Children12;
        children18Spinner = binding.grf4Children18;

        // hide views until we need them
        childcareSpinner.setVisibility(View.INVISIBLE);
        children1Spinner.setVisibility(View.INVISIBLE);
        children5Spinner.setVisibility(View.INVISIBLE);
        children12Spinner.setVisibility(View.INVISIBLE);
        children18Spinner.setVisibility(View.INVISIBLE);

        // set onItemSelectedListener for dropdowns. Hardcoded. TODO Change to loop
        // may need to update IDs .. thinking grf_4_input_dietary, etc. Then the textviews are
        // grf_4_textview_dietary. This way inputs are grouped and textviews are grouped.
        // hopefully then we can loop through them separately. Just need to know/determine how many
        // there are and can find id of first, then loop.
        children1Spinner.setOnItemSelectedListener(dropdownListener);
        children5Spinner.setOnItemSelectedListener(dropdownListener);
        children12Spinner.setOnItemSelectedListener(dropdownListener);
        children18Spinner.setOnItemSelectedListener(dropdownListener);
        numPeopleSpinner.setOnItemSelectedListener(numPeopleListener);
        childcareSpinner.setOnItemSelectedListener(dropdownListener);


        // set up on click listener for the 'next' button
        binding.nextButtonFourthFragmentGRegistration.setOnClickListener(v -> {

            // store the selected items into the instance variables
            householdNum = binding.grf4NumPeople.getSelectedItem().toString();
            childcareStatus = binding.grf4StatusChildcare.getSelectedItem().toString();
            children1 = binding.grf4Children1.getSelectedItem().toString();
            children5 = binding.grf4Children5.getSelectedItem().toString();
            children12 = binding.grf4Children12.getSelectedItem().toString();
            children18 = binding.grf4Children18.getSelectedItem().toString();

            // storing all strings in bundle to send to summary fragment
            result.putString("householdNum", householdNum);
            result.putString("childcareStatus", childcareStatus);
            result.putString("children1", children1);
            result.putString("children5", children5);
            result.putString("children12", children12);
            result.putString("children18", children18);

            // sending bundle
            getParentFragmentManager().setFragmentResult("sending_fourth_form_fragment_info", result);

            // navigate to the summary fragment when clicked
            NavHostFragment.findNavController(FourthFormFragment.this)
                    .navigate(R.id.action_DBR_FourthFormFragment_to_DBR_FifthFormFragment);

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


    private final AdapterView.OnItemSelectedListener numPeopleListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // if only one person, don't need to ask about other people in the household
            // pos 0 is placeholder, pos 1 = 1.
            if(position > 1) {
                childcareSpinner.setVisibility(View.VISIBLE);
            } else {
                childcareSpinner.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };



}