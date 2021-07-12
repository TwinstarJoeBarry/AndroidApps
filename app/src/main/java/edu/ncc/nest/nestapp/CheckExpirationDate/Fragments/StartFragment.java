package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2020 The LibreFoodPantry Developers.
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;

/**
 * StartFragment: This is the starting fragment for the CheckExpirationDate feature. This fragment
 * should ask the user whether or not they want to scan or enter a UPC barcode.
 *
 * Navigates to {@link ScannerFragment} when "Scan UPC" is selected.
 *
 * Navigates to {@link EnterUpcFragment} when "Enter UPC Manually" is selected.
 */
public class StartFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    public static final String LOG_TAG = StartFragment.class.getSimpleName();

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear any set fragment results since they are not needed in or prior to this fragment
        getParentFragmentManager().clearFragmentResult("FOOD ITEM");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_start,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the OnClickListener for button_scan
        view.findViewById(R.id.button_scan).setOnClickListener(view1 -> {

            // Navigate to ScannerFragment
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.CED_ScannerFragment);

        });

        // Set the OnClickListener for button_enter
        view.findViewById(R.id.button_enter).setOnClickListener(view12 -> {

            // Navigate to EnterUpcFragment
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.CED_EnterUpcFragment);

        });

    }

}