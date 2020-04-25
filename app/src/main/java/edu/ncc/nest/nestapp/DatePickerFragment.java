package edu.ncc.nest.nestapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int year;
    private int month;
    private int day;


    public Dialog onCreateDialog(Bundle savedInstanceState){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this, year, month, day);

    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){




    }




}
