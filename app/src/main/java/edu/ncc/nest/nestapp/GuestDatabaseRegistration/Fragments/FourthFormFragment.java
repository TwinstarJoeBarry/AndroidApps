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

import java.util.List;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.UIClasses.MultiSelectSpinner;
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

    private MultiSelectSpinner childcareMultiSelect;

    private TextView textview_numPeople, textview_childcare, textview_children1,
            textview_children5, textview_children12, textview_children18;

    // flags for hiding/showing views
    private boolean openView1, openView2, openView3, openView4, openView5 = false;

    // store numPeople selections to validate the number of total people vs children
    private int numPeople, numChildren1, numChildren5, numChildren12, numChildren18 = 0;

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
        numPeopleSpinner     = binding.grf4NumPeople;
        // TODO put back after multiselect merge
        //childcareMultiSelect = binding.grf4StatusChildcare;
        childcareSpinner     = binding.grf4StatusChildcare;
        children1Spinner     = binding.grf4Children1;
        children5Spinner     = binding.grf4Children5;
        children12Spinner    = binding.grf4Children12;
        children18Spinner    = binding.grf4Children18;
        textview_numPeople   = binding.grf4TextviewNumPeople;
        textview_childcare   = binding.grf4TextviewStatusChildcare;
        textview_children1   = binding.grf4TextviewChildren1;
        textview_children5   = binding.grf4TextviewChildren5;
        textview_children12  = binding.grf4TextviewChildren12;
        textview_children18  = binding.grf4TextviewChildren18;

        // load the multiselect using the class method
        // TODO put back after multiselect merge
        //childcareMultiSelect.setItems(getResources().getStringArray(R.array.childcare_status));

        // hide views until we need them
        // TODO put back after multiselect merge
        //childcareMultiSelect.setVisibility(View.GONE);
        childcareSpinner.setVisibility(View.GONE);
        children1Spinner.setVisibility(View.GONE);
        children5Spinner.setVisibility(View.GONE);
        children12Spinner.setVisibility(View.GONE);
        children18Spinner.setVisibility(View.GONE);
        textview_childcare.setVisibility(View.GONE);
        textview_children1.setVisibility(View.GONE);
        textview_children5.setVisibility(View.GONE);
        textview_children12.setVisibility(View.GONE);
        textview_children18.setVisibility(View.GONE);


        // set onItemSelectedListener for dropdowns.
        children1Spinner.setOnItemSelectedListener(children1Listener);
        children5Spinner.setOnItemSelectedListener(children5Listener);
        children12Spinner.setOnItemSelectedListener(children12Listener);
        children18Spinner.setOnItemSelectedListener(children18Listener);
        numPeopleSpinner.setOnItemSelectedListener(numPeopleListener);
        childcareSpinner.setOnItemSelectedListener(childcareListener);


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
                // TODO put back after multiselect merge
                //childcareMultiSelect.setVisibility(View.VISIBLE);
                childcareSpinner.setVisibility(View.VISIBLE);
                textview_childcare.setVisibility(View.VISIBLE);
                // reset selection to placeholder NOTE - Need animate:true to trigger the onItemSelectedListener
                childcareSpinner.setSelection(0, true);
            } else {
                // TODO put back after multiselect merge
                //childcareMultiSelect.setVisibility(View.GONE);
                childcareSpinner.setVisibility(View.GONE);
                textview_childcare.setVisibility(View.GONE);
                // set the selection to "i dont have children" to trigger the other ones
                childcareSpinner.setSelection(1, true);
            }
            // starts at 1 = 1, so position = people.
            numPeople = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener childcareListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position > 1) {
                // show others
                children1Spinner.setVisibility(View.VISIBLE);
                textview_children1.setVisibility(View.VISIBLE);
                children1Spinner.setSelection(1, true); // set to placeholder
            } else {
                // hide others
                children1Spinner.setVisibility(View.GONE);
                textview_children1.setVisibility(View.GONE);
                children1Spinner.setSelection(0, true); // set to 0
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener children1Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
            numChildren1 = position - 1;            //update numChildren1
            int otherPeople = numPeople - 1;        //remove self from total people
            int childrenNum = getNumChildren();     //calculate numChildren (total)

            if (position == 0) {
                children5Spinner.setVisibility(View.GONE);
                textview_children5.setVisibility(View.GONE);
                children5Spinner.setSelection(0, true);
            } else {
                children5Spinner.setVisibility(View.VISIBLE);
                textview_children5.setVisibility(View.VISIBLE);
                children5Spinner.setSelection(1, true); // set to 0
            }

            // check if we exceeded
            if (childrenNum > otherPeople) {
                // error, too many people
                children5Spinner.setVisibility(View.GONE);
                textview_children5.setVisibility(View.GONE);
                children5Spinner.setSelection(0, true); // set to placeholder
                Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                        "people in household, please check again.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (childrenNum == otherPeople) {
                // valid, but need to disable other options
                // TODO enable next button
                children5Spinner.setSelection(0, true);
                children5Spinner.setVisibility(View.GONE);
                textview_children5.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener children5Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
            numChildren5 = position - 1;            //update numChildren1
            int otherPeople = numPeople - 1;        //remove self from total people
            int childrenNum = getNumChildren();     //calculate numChildren (total)

            if (position == 0) {
                children12Spinner.setVisibility(View.GONE);
                textview_children12.setVisibility(View.GONE);
                children12Spinner.setSelection(0, true);
            } else {
                children12Spinner.setVisibility(View.VISIBLE);
                textview_children12.setVisibility(View.VISIBLE);
                children12Spinner.setSelection(1, true);
            }

            // check if we exceeded
            if (childrenNum > otherPeople) {
                // error, too many people
                children12Spinner.setVisibility(View.GONE);
                textview_children12.setVisibility(View.GONE);
                children12Spinner.setSelection(0, true);
                Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                        "people in household, please check again.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (childrenNum == otherPeople) {
                // valid, but need to disable other options
                // TODO enable next button
                children12Spinner.setVisibility(View.GONE);
                textview_children12.setVisibility(View.GONE);
                children12Spinner.setSelection(0, true);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener children12Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
            numChildren12 = position - 1;            //update numChildren1
            int otherPeople = numPeople - 1;        //remove self from total people
            int childrenNum = getNumChildren();     //calculate numChildren (total)

            if (position == 0) {
                children18Spinner.setVisibility(View.GONE);
                textview_children18.setVisibility(View.GONE);
                children18Spinner.setSelection(1, true);
            } else {
                children18Spinner.setVisibility(View.VISIBLE);
                textview_children18.setVisibility(View.VISIBLE);
                children18Spinner.setSelection(0, true);
            }

            // check if we exceeded
            if (childrenNum > otherPeople) {
                // error, too many people
                children18Spinner.setVisibility(View.GONE);
                textview_children18.setVisibility(View.GONE);
                children18Spinner.setSelection(1, true);
                Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                        "people in household, please check again.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (childrenNum == otherPeople) {
                // valid, but need to disable other options
                // TODO enable next button
                children18Spinner.setSelection(1, true);
                children18Spinner.setVisibility(View.GONE);
                textview_children18.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener children18Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
            numChildren18 = position - 1;            //update numChildren1
            int otherPeople = numPeople - 1;        //remove self from total people
            int childrenNum = getNumChildren();     //calculate numChildren (total)

            /*
            if (position == 0) {
                children18Spinner.setVisibility(View.GONE);
                textview_children18.setVisibility(View.GONE);
            } else {
                children18Spinner.setVisibility(View.VISIBLE);
                textview_children18.setVisibility(View.VISIBLE);
            }
             */

            // check if we exceeded
            if (childrenNum > otherPeople) {
                // error, too many people
                // TODO disable next button
                Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                        "people in household, please check again.", Toast.LENGTH_SHORT);
                toast.show();
            } else if (childrenNum == otherPeople) {
                // valid
                // TODO enable next button
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private int getNumChildren() {
        return numChildren1 + numChildren5 + numChildren12 + numChildren18;
    }

    // NEED TO WAIT UNTIL DIALOG BRANCH IS MERGED FOR THIS. THAT OR TRY TO MERGE THESE TWO
    /*
    private final AdapterView.OnItemClickListener childcareClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<Integer> selections = ((MultiSelectSpinner) view).getSelectedIndices();

                for (int i = 0; i < selections.size(); i++) {
                    // if any selection other than first option (no children) is selected
                    if(selections.get(i) > 0) {
                        // show others
                        children1Spinner.setVisibility(View.VISIBLE);
                        textview_children1.setVisibility(View.VISIBLE);
                    } else {
                        // hide others
                        children1Spinner.setVisibility(View.GONE);
                        textview_children1.setVisibility(View.GONE);
                    }
                }
            }
    };
     */
}