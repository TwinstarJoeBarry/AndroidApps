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

import edu.ncc.nest.nestapp.R;

/**
 * SummaryFragment: This fragment represent a summary of the registration process. Displays messages
 * to the guest depending on whether or not the registration process was successful or not.
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

    public static final String TAG = SummaryFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "In SummaryFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_summary, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // OnClickListener for the "Done" button
        view.findViewById(R.id.button).setOnClickListener(clickedView -> {



        });

    }

}