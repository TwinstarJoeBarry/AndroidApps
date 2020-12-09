package edu.ncc.nest.nestapp.FragmentsUpc;
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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ncc.nest.nestapp.DatePickerFragment;
import edu.ncc.nest.nestapp.R;

import android.widget.DatePicker;


public class SelectPrintedExpirationDateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private String selectedDate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_printed_expiration_date, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // select date button pressed
        view.findViewById(R.id.button_select_date).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Create a DatePicker object
                DialogFragment datePicker = new DatePickerFragment();
                // Show datePicker
                datePicker.show(getParentFragmentManager(), "date picker");

                

            }
        });


        view.findViewById(R.id.button_confirm_date).setOnClickListener(new View.OnClickListener() {

            // TODO: if statement to see if a date was actually selected, if no date then send toast to use date picker

            @Override
            public void onClick(View view) {
                // navigation has yet to be set up in the nav_graph.xml
                NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}