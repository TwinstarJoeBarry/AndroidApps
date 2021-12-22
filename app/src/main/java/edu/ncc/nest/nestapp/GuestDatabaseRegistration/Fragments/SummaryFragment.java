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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;


import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.GuestFormTesting;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFourthFormBinding;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSummaryBinding;

/**
 * SummaryFragment: This fragment represent a summary of the registration process. Displays messages
 * to the guest depending on whether or not the registration process was successful or not. Should
 * also generate a barcode for the guest if needed.
 *
 * TODO: ///////////////////////////////////////////////////////////////////////////////////////////
 *  - This fragment should retrieve all the guest's information passed in a FragmentResult from
 *    previous fragments.
 *  - Check to make sure that the guest isn't already registered in the database (using name).
 *  - Generate a unique barcode not currently in use for the guest (if the user did not scan one).
 *  - Register the guest with all their information and barcode into the local registry database.
 *  - If the guest is already registered, this fragment should show a message saying that the guest
 *    is already registered.
 *  - If the registration is successful this fragment should display a message saying that
 *    registration was successful and display the users newly generated barcode back to the guest.
 * TODO: ///////////////////////////////////////////////////////////////////////////////////////////
 */
public class SummaryFragment extends Fragment  {
    private FragmentGuestDatabaseRegistrationSummaryBinding binding;
    private OnBackPressedCallback backbuttonCallback;

    // first fragment information
    private String fname;
    private String lname;
    private String phoneNum;
    private String nccId;

    // second fragment information
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zip;
    private String affiliation;
    private String age;
    private String gender;

    // third fragment information
    private String dietary;
    private String snap;
    private String otherProg;
    private String employment;
    private String health;
    private String housing;
    private String income;

    // fourth fragment information
    private String householdNum;
    private String childcareStatus;
    private String children1;
    private String children5;
    private String children12;
    private String children18;

    // barcode info
    private String barcode;

    // fifth fragment information
    private String referrer;
    private String comments;
    private String volunteerFName;
    private String volunteerLName;

    // Database where we will store user information
    private GuestRegistrySource db;

    public static final String TAG = SummaryFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "In SummaryFragment onCreateView()");

        binding = FragmentGuestDatabaseRegistrationSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_guest_database_registration_summary, container, false);

    }
    /*
        Test work done from obtaining first and second form information.
            * Tested the First Name(obtained from FirstFormFragment)
            * Tested the City(obtained from SecondFormFragment)
     */
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

        // TODO do we need a back verification here? prob not if fields are not editable. otherwise copy/paste from previous fragments
        //Toast.makeText(getContext(), "WARNING: Pressing back will clear data. Please double check before continuing.", Toast.LENGTH_LONG).show();
        // Creating the database and passing the correct context as the argument
        db = new GuestRegistrySource(requireContext());

        //retrieving first name, last name, phone number and NCC ID from FirstFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_first_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        fname = result.getString("First Name");
                        lname = result.getString("Last Name");
                        phoneNum = result.getString("Phone Number");
                        nccId = result.getString("NCC ID");
                        Log.d(TAG, "The first name obtained is: " + fname);
                        Log.d(TAG, "The NCC ID is: " + nccId);

                        binding.grf1FName.setText(fname);
                        binding.grf1LName.setText(lname);
                        binding.grf1Phone.setText("(" + phoneNum.substring(0, 3) + ") " + phoneNum.substring(3, 6) + "-" + phoneNum.substring(6));
                        binding.grf1NccId.setText("N" + nccId);
                    }
                });

        //retrieving streetAddress1&2, city, state zip, affiliation, age, and gender from SecondFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_second_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        streetAddress1 = result.getString("Street Address 1");
                        streetAddress2 = result.getString("Street Address 2");  //optional
                        city = result.getString("City");
                        state = result.getString("State");
                        zip = result.getString("Zip");
                        affiliation = result.getString("Affiliation");
                        age = result.getString("Age");
                        gender = result.getString("Gender");
                        Log.d(TAG, "The city obtained is: " + city);
                        Log.d(TAG, "The age obtained is: " + age);

                        binding.grf2Address1.setText(streetAddress1);

                        if(streetAddress2 != null){
                            binding.grf2Address2.setText(streetAddress2);
                        }

                        binding.grf2City.setText(city);
                        binding.grf2State.setText(state);
                        binding.grf2Zip.setText(zip);
                        binding.grf2Affiliation.setText(affiliation);
                        binding.grf2Age.setText(age);
                        binding.grf2Gender.setText(gender);
                    }
                });


        // retrieving dietary, other programs, snap, employment, health, and housing info from ThirdFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_third_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        dietary = result.getString("dietary");
                        snap = result.getString("snap");
                        otherProg = result.getString("programs");
                        employment = result.getString("employment");
                        health = result.getString("health");
                        housing = result.getString("housing");
                        income = result.getString("income");

                        Log.d(TAG, "The dietary information obtained is: " + dietary);
                        Log.d(TAG, "The employment obtained is: " + employment);
                        binding.grf3Dietary.setText(dietary.toString());
                        binding.grf3Snap.setText(snap);
                        binding.grf3OtherProgs.setText(otherProg);
                        binding.grf3StatusEmployment.setText(employment.toString());
                        binding.grf3StatusHealth.setText(health.toString());
                        binding.grf3StatusHousing.setText(housing.toString());
                        binding.grf3Income.setText(income);
                        Log.d(TAG, "The income is: " + income);

                    }
                });

        // retrieving household number, childcare status, and age info of children from FourthFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_fourth_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        householdNum = result.getString("householdNum");
                        childcareStatus = result.getString("childcareStatus");
                        children1 = result.getString("children1");
                        children5 = result.getString("children5");
                        children12 = result.getString("children12");
                        children18 = result.getString("children18");

                        binding.grf4NumPeople.setText(householdNum);
                        binding.grf4StatusChildcare.setText(childcareStatus);
                        binding.grf4Children1.setText(children1);
                        binding.grf4Children5.setText(children5);
                        binding.grf4Children12.setText(children12);
                        binding.grf4Children18.setText(children18);
                        Log.d(TAG, "The childcare status obtained is: " + childcareStatus);
                        Log.d(TAG, "The amount of children between 13m and 5 obtained is: " + children5);

                    }
                });

        getParentFragmentManager().setFragmentResultListener("sending_barcode_info",
                this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        barcode = result.getString("registrationBarcode");
                        Log.d("**BARCODE**", "BARCODE: " + barcode);
                    }
        });

        getParentFragmentManager().setFragmentResultListener("sending_fifth_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        referrer = result.getString("Referrer");
                        comments = result.getString("Comments");
                        volunteerFName = result.getString("Volunteer First Name");
                        volunteerLName = result.getString("Volunteer Last Name");

                        binding.grf5Referrer.setText(referrer);
                        binding.grf5Comments.setText(comments);
                        binding.grf5VolunteerFName.setText(volunteerFName);
                        binding.grf5VolunteerLName.setText(volunteerLName);
                    }
                });


        // OnClickListener for the "Done" button
        view.findViewById(R.id.button).setOnClickListener(clickedView -> {

            // registering the guest to the database
            // TODO: null values needs to be retrieved and replaced.
            String nameOfVolunteer = volunteerFName + " " + volunteerLName;

            db.insertData(fname + " " + lname, phoneNum, nccId, streetAddress1 + ", " + streetAddress2,
                    city, zip, state, affiliation, age, gender, dietary, otherProg, snap, employment, health, housing,
                    income, householdNum, childcareStatus, children1, children5, children12, children18,
                    comments, nameOfVolunteer, barcode);

            // go back to 'guest forms' page. decided this makes more sense than app home.
            // see method definition below for explanation how to switch to to navigate to app home instead
            createConfirmedDialog(); // show a dialog first so they know it worked.

            // Navigate back to splash screen.
            // later, make if/else to go to scanner or login if scanner already in db
            /*
            NavHostFragment.findNavController(SummaryFragment.this)
                    .navigate(R.id.action_DBR_SummaryFragment_to_DBR_StartFragment);

             */
        });
        // OnClickListener for the "Done" button
        //TODO store in database when done button is clicked
//        view.findViewById(R.id.button).setOnClickListener(clickedView -> {
//
//            // Navigate back to splash screen.
//            // later, make if/else to go to scanner or login if scanner already in db
//            NavHostFragment.findNavController(SummaryFragment.this)
//                    .navigate(R.id.action_DBR_SummaryFragment_to_DBR_StartFragment);
//
//        });

    }

    /**
     * home method --
     * description: this method goes to the nest home screen
     */
    public void home() {
        // sending to guestform testing so they can login. Otherwise switch to "Choose.class" to go to app home.
        Intent intent = new Intent(getActivity(), GuestFormTesting.class);
        startActivity(intent);
    }

    public boolean createConfirmedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thank you!");
        builder.setMessage("Your registration has been confirmed. You will now be taken back to the guest forms page. You can now log in and start your first visit!");

        // close button
        builder.setPositiveButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // just close the dialog
                dialog.dismiss();
                // go back to guest forms page
                home();
            }
        });

        builder.show();
        return true;
    }

}