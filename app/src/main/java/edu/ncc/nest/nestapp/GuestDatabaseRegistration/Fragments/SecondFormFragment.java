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

import edu.ncc.nest.nestapp.R;

/**
 * SecondFormFragment: Represents a form that a guest can fill in with their household information.
 * The fragment then bundles all of the user's inputs (including info passed from
 * {@link FirstFormFragment} and sends them to the next fragment {@link SummaryFragment}.
 */
public class SecondFormFragment extends Fragment {

    public static final String TAG = SecondFormFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_database_registration_second_form, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // listener to retrieve the bundle that's send from FirstFormFragment
        getParentFragmentManager().setFragmentResultListener("firstFormInfo", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                // the data sent from FirstFormFragment
                String name = result.getString("NAME");
                String phone = result.getString("PHONE");
                String email = result.getString("EMAIL");
                String id = result.getString("ID");
                String address = result.getString("ADDRESS");
                String city = result.getString("CITY");
                String zip = result.getString("ZIP");
                String date = result.getString("DATE");

                // For testing purposes
                Log.d(TAG, "The name is: " + name);
                Log.d(TAG, "The phone number is: " + phone);
                Log.d(TAG, "The id  is: " + id);
                Log.d(TAG, "The zip is: " + zip);
            }
        });
    }
}