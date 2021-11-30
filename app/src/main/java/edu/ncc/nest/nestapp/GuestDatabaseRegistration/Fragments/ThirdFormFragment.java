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

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationThirdFormBinding;

/**
 * ThirdFormFragment: Represents a form that a guest can fill in with more of their information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link SecondFormFragment} and sends them to the next fragment (will be Fourth Fragment).
 */
public class ThirdFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationThirdFormBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGuestDatabaseRegistrationThirdFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_guest_database_registration_third_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set onItemSelectedListener for dropdowns. Hardcoded. Change to loop
        binding.grf3Dietary.setOnItemSelectedListener(dropdownListener);
        binding.grf3OtherProgs.setOnItemSelectedListener(dropdownListener);
        binding.grf3Snap.setOnItemSelectedListener(dropdownListener);
        binding.grf3StatusEmployment.setOnItemSelectedListener(dropdownListener);
        binding.grf3StatusHealth.setOnItemSelectedListener(dropdownListener);
        binding.grf3StatusHousing.setOnItemSelectedListener(dropdownListener);

        // adds the onClick listener to the 'next' button
        binding.nextButtonThirdFragmentGRegistration.setOnClickListener(v -> {

            // navigate to the fourth fragment when clicked
            NavHostFragment.findNavController(ThirdFormFragment.this)
                    .navigate(R.id.action_DBR_ThirdFormFragment_to_fourthFormFragment);
        });
    }

    // This dropdown listener currently changes the first item in the spinner to muted text.
    // When a user selects an item other than the first, text changes to standard color.
    // Later, we can use this for verification. Can also attach a toast for selection.
    private AdapterView.OnItemSelectedListener dropdownListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0) {
                ((TextView) view).setTextColor(Color.GRAY);
                Log.d("**SELECTED**", "HINT");
            } else {
                ((TextView) view).setTextColor(Color.BLACK);
                Log.d("**SELECTED**", "OTHER");
                // Notify the selected item text
                Toast.makeText
                        (getContext(), "Selected : " + ((TextView) view).getText(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}