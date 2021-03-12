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

public class StartFragment extends Fragment {

    protected static final String TAG = "TESTING";

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG, "In StartFragment onCreateView()");

        // Inflate the layout for this fragment - This is where the navigation begins
        return inflater.inflate(R.layout.fragment_guest_database_registration_start_page, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Navigates to the Guest Registration Entry Form for person information
        view.findViewById(R.id.entry_form_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Uncomment this code when the layouts for the registration form page is available &
                // complete this line with the appropriate nav action -> navigate( R.id.action_StartFragment_to_FormFragment)
                NavHostFragment.findNavController(StartFragment.this)
                       .navigate(R.id.action_DBR_StartFragment_to_FirstFormFragment);
            }
        });

        // Navigates to the Scanner Fragment that reads in the data passed by a barcode
        view.findViewById(R.id.barcode_scanner_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StartFragment.this)
                        .navigate(R.id.action_DBR_StartFragment_to_ScannerFragment);
            }
        });
    }




}
