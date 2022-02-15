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
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

    private Button nextBtn;

    // used as a final check on the next button
    private boolean goodData = false;

    private final String TAG = "**NumChildren Test**";

    // flags for hiding/showing views
    private boolean openView1, openView2, openView3, openView4, openView5 = false;
    // flag for disabling nextButton
    private boolean isDisabled = false;

    // store numPeople selections to validate the number of total people vs children
    private int numChildren1, numChildren5, numChildren12, numChildren18 = 0;
    private int numPeople = 1;  // default to 1 person (user)

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
        childcareMultiSelect = binding.grf4StatusChildcare;
        //childcareSpinner     = binding.grf4StatusChildcare;
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

        nextBtn = binding.nextButtonFourthFragmentGRegistration;

        // load the multiselect using the class method
        childcareMultiSelect.setItems(getResources().getStringArray(R.array.childcare_status));

        // hide views until we need them
        childcareMultiSelect.setVisibility(View.GONE);
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
        //childcareSpinner.setOnItemSelectedListener(childcareListener);
        (childcareMultiSelect).setOnTouchListener(childcareTouchListener);
        // TODO I can define a custom onclick listener in the MultiSelect class!


        // set up on click listener for the 'next' button
        binding.nextButtonFourthFragmentGRegistration.setOnClickListener(v -> {

            // store the selected items into the instance variables
            householdNum = binding.grf4NumPeople.getSelectedItem().toString();
            childcareStatus = childcareMultiSelect.getSelectedItemsAsString(); // updated to multiselect call
            children1 = binding.grf4Children1.getSelectedItem().toString();
            children5 = binding.grf4Children5.getSelectedItem().toString();
            children12 = binding.grf4Children12.getSelectedItem().toString();
            children18 = binding.grf4Children18.getSelectedItem().toString();

            // storing all strings in bundle to send to summary fragment
            result.putString("householdNum", householdNum);
            result.putString("childcareStatus", childcareStatus);
            // pulling from instance variables as these will always be accurate
            result.putString("children1", Integer.toString(numChildren1));
            result.putString("children5", Integer.toString(numChildren5));
            result.putString("children12", Integer.toString(numChildren12));
            result.putString("children18", Integer.toString(numChildren18));

            // just make sure the first and second field are selected. The rest is handled by waterfall logic.
            if (numPeopleSpinner.getSelectedItemPosition() == 0 || childcareMultiSelect.getSelectedIndices().isEmpty()) {
                goodData = false;
            } else {
                goodData = true;
            }

            // sending bundle
            if (goodData) {
                getParentFragmentManager().setFragmentResult("sending_fourth_form_fragment_info", result);

                // navigate to the summary fragment when clicked
                NavHostFragment.findNavController(FourthFormFragment.this)
                        .navigate(R.id.action_DBR_FourthFormFragment_to_DBR_FifthFormFragment);
            } else {
                Toast toast = Toast.makeText(getContext(), "Please make a selection before continuing.", Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    private final AdapterView.OnItemSelectedListener numPeopleListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // if only one person, don't need to ask about other people in the household
            // pos 0 is placeholder, pos 1 = 1.
            if(position > 1) {
                // TODO put back after multiselect merge
                childcareMultiSelect.setVisibility(View.VISIBLE);
                //childcareSpinner.setVisibility(View.VISIBLE);
                textview_childcare.setVisibility(View.VISIBLE);
                // reset selection to placeholder NOTE - Need animate:true to trigger the onItemSelectedListener
                childcareMultiSelect.clearSelections();
                //childcareSpinner.setSelection(0, true);
            } else {
                // TODO put back after multiselect merge
                childcareMultiSelect.setVisibility(View.GONE);
                //childcareSpinner.setVisibility(View.GONE);
                textview_childcare.setVisibility(View.GONE);

                // set the selection to "i dont have children" to trigger the other ones
                childcareMultiSelect.setSelection(0);

                // TODO add if disabled, enable code
                if (isDisabled) {
                    setEnabled(nextBtn);
                }
            }
            // starts at 1 = 1, so position = people.
            // already initialized to 1, so if position is 0 do nothing, else update numPeople to position.
            if (position > 0) {
                numPeople = position;
            }

            // check for a number conflict each time. Handles if the user went down the chain and then changed top number
            if (isNumConflict()) {
                // TODO disable next button
                if (!isDisabled) {
                    setDisabled(nextBtn);
                }
                // show toast
                Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                        "people in household, please check again.", Toast.LENGTH_SHORT);
                toast.show();
            }

            // reset instance values to avoid early error checking
            // doing it every time to avoid conflicts and fixing bug 2
            resetNumChildren();

            // force multiselect verification after changing selection.
            verifyMultiselect.run();

            // TODO Remove Test Log
            Log.d(TAG, displayChildrenNums());

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnTouchListener childcareTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("**ONTOUCH**", "In On Touch EVENT");

            if (event.getAction() == MotionEvent.ACTION_UP) {
                // call the perform click function using a callback. Callback will be run after dismiss() is pressed.
                boolean done = ((MultiSelectSpinner) v).performClickCallback(verifyMultiselect);
            }
            return true;
        }
    };

    // runnable task that runs after called. Used as a callback to the multiselect dialog
    private Runnable verifyMultiselect = new Runnable() {
        @Override
        public void run() {
            Log.d("**ONTOUCH**", "In 'runnabl task'");
            MultiSelectSpinner v = childcareMultiSelect;
            List<Integer> selections = ((MultiSelectSpinner) v).getSelectedIndices();

            int count = selections.size();
            int iterator = 0;
            boolean looping = true;

            for (int i = 0; i < selections.size(); i++) {
                //while (count > iterator && looping) {
                // if any selection other than first option (no children) appears
                //Log.d(TAG, "SELECTIONS: " + selections.get(i).toString());
                // dont need to loop? Works better when not looping. First item will always be first position
                if (selections.get(0) == 0) {
                    // hide others
                    children1Spinner.setVisibility(View.GONE);
                    textview_children1.setVisibility(View.GONE);
                    childcareMultiSelect.setSelection(0);
                    looping = false;
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                } else {
                    // show others
                    children1Spinner.setVisibility(View.VISIBLE);
                    textview_children1.setVisibility(View.VISIBLE);
                }
                // reset next field
                children1Spinner.setSelection(0, true);
            }
            Log.d("**ONTOUCH**", "END 'runnable task'");
        }
    };


        private final AdapterView.OnItemSelectedListener children1Listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
                numChildren1 = position - 1;            //update numChildren1
                // reset it to 0 so it doesn't become -1
                if (numChildren1 < 0) {
                    numChildren1 = 0;
                }
                int otherPeople = numPeople - 1;        //remove self from total people
                int childrenNum = getNumChildren();     //calculate numChildren (total)

                // simple if else, hide next field if we are on the placeholder
                if (position == 0) {
                    children5Spinner.setVisibility(View.GONE);
                    textview_children5.setVisibility(View.GONE);
                } else {
                    children5Spinner.setVisibility(View.VISIBLE);
                    textview_children5.setVisibility(View.VISIBLE);
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                }

                // check if we exceeded. will override the simple if/else if there is a conflict
                if (childrenNum > otherPeople) {
                    // error, too many people
                    children5Spinner.setVisibility(View.GONE);
                    textview_children5.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                            "people in household, please check again.", Toast.LENGTH_SHORT);
                    toast.show();
                    // TODO disable next button
                    if (!isDisabled) {
                        setDisabled(nextBtn);
                    }

                } else if (childrenNum == otherPeople) {
                    // valid, but need to disable other options
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                    children5Spinner.setVisibility(View.GONE);
                    textview_children5.setVisibility(View.GONE);
                }

                // reset next
                children5Spinner.setSelection(0, true);

                // TODO Remove Test Log
                Log.d(TAG, displayChildrenNums());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        private final AdapterView.OnItemSelectedListener children5Listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
                numChildren5 = position - 1;            //update numChildren5
                // reset it to 0 so it doesn't become -1
                if (numChildren5 < 0) {
                    numChildren5 = 0;
                }
                int otherPeople = numPeople - 1;        //remove self from total people
                int childrenNum = getNumChildren();     //calculate numChildren (total)

                // simple if/else. Hide next if on placeholder
                if (position == 0) {
                    children12Spinner.setVisibility(View.GONE);
                    textview_children12.setVisibility(View.GONE);
                } else {
                    children12Spinner.setVisibility(View.VISIBLE);
                    textview_children12.setVisibility(View.VISIBLE);
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                }

                // check if we exceeded. will override simple if/else if conflict
                if (childrenNum > otherPeople) {
                    // error, too many people
                    children12Spinner.setVisibility(View.GONE);
                    textview_children12.setVisibility(View.GONE);
                    // TODO calls sometimes when first field is set to 1. Fix
                    Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                            "people in household, please check again.", Toast.LENGTH_SHORT);
                    toast.show();
                    // TODO add disable next button
                    if (!isDisabled) {
                        setDisabled(nextBtn);
                    }

                } else if (childrenNum == otherPeople) {
                    // valid, but need to disable other options
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                    children12Spinner.setVisibility(View.GONE);
                    textview_children12.setVisibility(View.GONE);
                }

                // reset next
                children12Spinner.setSelection(0, true);

                // TODO Remove Test Log
                Log.d(TAG, displayChildrenNums());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        private final AdapterView.OnItemSelectedListener children12Listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 0 = placeholder, 1 = 0. Therefore, num = pos - 1
                numChildren12 = position - 1;            //update numChildren12
                // reset it to 0 so it doesn't become -1
                if (numChildren12 < 0) {
                    numChildren12 = 0;
                }
                int otherPeople = numPeople - 1;        //remove self from total people
                int childrenNum = getNumChildren();     //calculate numChildren (total)

                // simple if/else. Hide next if on placeholder
                if (position == 0) {
                    children18Spinner.setVisibility(View.GONE);
                    textview_children18.setVisibility(View.GONE);
                } else {
                    children18Spinner.setVisibility(View.VISIBLE);
                    textview_children18.setVisibility(View.VISIBLE);
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                }

                // check if we exceeded. will override simple if/else if conflict
                if (childrenNum > otherPeople) {
                    // error, too many people
                    children18Spinner.setVisibility(View.GONE);
                    textview_children18.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                            "people in household, please check again.", Toast.LENGTH_SHORT);
                    toast.show();

                    // TODO disable next button
                    if (!isDisabled) {
                        setDisabled(nextBtn);
                    }

                } else if (childrenNum == otherPeople) {
                    // valid, but need to disable other options
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                    children18Spinner.setVisibility(View.GONE);
                    textview_children18.setVisibility(View.GONE);
                }
                // reset next
                children18Spinner.setSelection(0, true);

                // TODO Remove Test Log
                Log.d(TAG, displayChildrenNums());
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
                // reset it to 0 so it doesn't become -1
                if (numChildren18 < 0) {
                    numChildren18 = 0;
                }
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
                    if (!isDisabled) {
                        setDisabled(nextBtn);
                    }

                    Toast toast = Toast.makeText(getContext(), "Can not have more children than " +
                            "people in household, please check again.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (childrenNum == otherPeople) {
                    // valid
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                } else {
                    // TODO add if disabled, enable code
                    if (isDisabled) {
                        setEnabled(nextBtn);
                    }
                }

                // TODO Remove Test Log
                Log.d(TAG, displayChildrenNums());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        private int getNumChildren() {
            return numChildren1 + numChildren5 + numChildren12 + numChildren18;
        }

        private boolean isNumConflict() {
            // i know, can just call method above, but maybe this is faster?
            return (numChildren1 + numChildren5 + numChildren12 + numChildren18) > numPeople;
        }

        private void resetNumChildren() {
            numChildren1 = 0;
            numChildren5 = 0;
            numChildren12 = 0;
            numChildren18 = 0;
        }

        private String displayChildrenNums() {
            StringBuilder str = new StringBuilder();
            str.append("CHILDREN STORED:\n");
            str.append(numChildren1 + "\n");
            str.append(numChildren5 + "\n");
            str.append(numChildren12 + "\n");
            str.append(numChildren18 + "\n");

            return str.toString();
        }

        private void setEnabled(View v) {
            v.setEnabled(true);
            isDisabled = false;
            v.setBackgroundColor(getResources().getColor(R.color.colorPrimary, requireActivity().getTheme()));
        }

        private void setDisabled(View v) {
            v.setEnabled(false);
            isDisabled = true;
            v.setBackgroundColor(Color.GRAY);
        }

    }