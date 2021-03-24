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
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;

/**
 * StartFragment: Allows the user to choose whether they want to scan their own barcode or
 * if they want to get a new barcode generated instead.
 *
 * Navigates to {@link FirstFormFragment} if the user wants a new barcode generated instead.
 *
 * Navigates to {@link ScannerFragment} if the user wants to scan their own barcode.
 */
public class StartFragment extends Fragment {

    protected static final String TAG = "TESTING";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "In StartFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_start, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Navigates to the guest registration forms
        view.findViewById(R.id.entry_form_btn).setOnClickListener(clickedView -> {

            NavHostFragment.findNavController(StartFragment.this)
                   .navigate(R.id.action_DBR_StartFragment_to_FirstFormFragment);

        });

        // Navigates to the ScannerFragment that reads a barcode
        view.findViewById(R.id.barcode_scanner_btn).setOnClickListener(clickedView -> {

            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.action_DBR_StartFragment_to_ScannerFragment);

        });

    }

}
