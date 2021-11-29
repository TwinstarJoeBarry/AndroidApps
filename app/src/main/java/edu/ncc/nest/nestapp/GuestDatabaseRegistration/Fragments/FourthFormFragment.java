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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFourthFormBinding;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSecondFormBinding;

/**
 * ThirdFormFragment: Represents a form that a guest can fill in with more of their information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link ThirdFormFragment} and sends them to the next fragment {@link SummaryFragment}.
 */
public class FourthFormFragment extends Fragment {

    private FragmentGuestDatabaseRegistrationFourthFormBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // implement binding to inflate layout
        binding = FragmentGuestDatabaseRegistrationFourthFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_guest_database_registration_fourth_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set up on click listener for the 'next' button
        binding.nextButtonFourthFragmentGRegistration.setOnClickListener(v -> {

            // navigate to the summary fragment when clicked
            NavHostFragment.findNavController(FourthFormFragment.this)
                    .navigate(R.id.action_DBR_FourthFormFragment_to_DBR_SummaryFragment);

        });
    }

}