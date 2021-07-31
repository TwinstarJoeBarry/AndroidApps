package edu.ncc.nest.nestapp;

/**
 *

 Copyright (C) 2020 The LibreFoodPantry Developers.


 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.


 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.


 You should have received a copy of the GNU General Public License
 along with this program.  If not, see https://www.gnu.org/licenses/.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);

        int month = c.get(Calendar.MONTH);

        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireActivity(),
                android.R.style.Theme_Material_Dialog_Alert, this,
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

}
