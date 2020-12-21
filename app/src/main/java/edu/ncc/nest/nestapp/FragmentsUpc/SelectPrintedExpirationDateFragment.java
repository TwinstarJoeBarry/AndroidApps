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
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

import edu.ncc.nest.nestapp.R;

public class SelectPrintedExpirationDateFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
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

        view.findViewById(R.id.button_select_date_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigation has yet to be set up in the nav_graph.xml
                NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
            }
        });

        view.findViewById(R.id.date_select_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
    }

    /**
     * showDatePickerDialog method --
     * onClick for the Date button; creates and shows a DatePickerDialog initialized to the current
     * date.
     *
     * @param v - the Date button
     */
    public void showDatePickerDialog(View v){
        DatePickerDialog datePicker = new DatePickerDialog(
                v.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}