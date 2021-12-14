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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;


import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

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
    private String programs;
    private String snap;
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

    // Database where we will store user information
    private GuestRegistrySource db;

    public static final String TAG = SummaryFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "In SummaryFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_summary, container, false);

    }
    /*
        Test work done from obtaining first and second form information.
            * Tested the First Name(obtained from FirstFormFragment)
            * Tested the City(obtained from SecondFormFragment)
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                    }
                });

        // retrieving dietary, other programs, snap, employment, health, and housing info from ThirdFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_third_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        dietary = result.getString("dietary");
                        programs = result.getString("programs");
                        snap = result.getString("snap");
                        employment = result.getString("employment");
                        health = result.getString("health");
                        housing = result.getString("housing");
                        income = result.getString("income");
                        Log.d(TAG, "The dietary information obtained is: " + dietary);
                        Log.d(TAG, "The employment obtained is: " + employment);
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
                        Log.d(TAG, "The childcare status obtained is: " + childcareStatus);
                        Log.d(TAG, "The amount of children between 13m and 5 obtained is: " + children5);

                    }
                });


        // OnClickListener for the "Done" button
        view.findViewById(R.id.button).setOnClickListener(clickedView -> {

            // registering the guest to the database
            // TODO: null values needs to be retrieved and replaced.
            db.insertData(fname + " " + lname, phoneNum, nccId, streetAddress1 + ", " + streetAddress2,
                    city, zip, state, affiliation, age, gender, dietary, programs, snap, employment, health, housing,
                    income, householdNum, childcareStatus, children1, children5, children12, children18,
                    null, null, null);


            // Navigate back to splash screen.
            // later, make if/else to go to scanner or login if scanner already in db
            NavHostFragment.findNavController(SummaryFragment.this)
                    .navigate(R.id.action_DBR_SummaryFragment_to_DBR_StartFragment);

        });

    }

}