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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

/**
 * SecondFormFragment: Represents a form that a guest can fill in with their household information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link FirstFormFragment} and sends them to the next fragment {@link SummaryFragment}.
 */
public class SecondFormFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = SecondFormFragment.class.getSimpleName();

    private GuestRegistrySource db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_second_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Creating the database and passing the correct context as the argument
        db = new GuestRegistrySource(requireContext());

        // for testing purposes
        getParentFragmentManager().setFragmentResultListener("registration_form", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.d(TAG, "The name is: " + result.getString("NAME"));
            }
        });
    }

    @Override
    public void onClick(View view) {

        // TODO This method needs to be updated or replaced once SecondFormFragment is completed.

        // Variable used for checks
        boolean ins = false;

        // Adding the values into the database if submit button is pressed
        if (view.getId() == R.id.done_button) {

            // NOTE: The parameter 'barcode' was recently added to this method
            // TODO: Update parameter 'barcode' to the barcode representing this user
            // ins = db.insertData(name.getText().toString(), email.getText().toString(), phone.getText().toString(), date.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), null);

        }

        // Notifying the user if the add was successful
        if (ins) {

            // For testing purposes, comment out if not needed.
            Toast.makeText(requireContext(), "User Added", Toast.LENGTH_LONG).show();

        }

    }

}