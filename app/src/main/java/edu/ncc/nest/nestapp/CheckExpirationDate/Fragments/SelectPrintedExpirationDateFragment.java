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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.Calendar;

import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectPrintedExpirationDateFragment: Allows the user to select or enter the item's printed
 * expiration date, then sends the result to {@link StatusFragment}.
 */
public class SelectPrintedExpirationDateFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = SelectPrintedExpirationDateFragment.class.getSimpleName();

    private LocalDate printedExpDate = LocalDate.now();

    private NumberPicker dayPicker;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_check_expiration_date_select_printed_expiration_date,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listen for the foodItem from the bundle sent from the previous fragment
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            /* This request key is only required when navigating back from
             * StatusFragment */
            if (result.containsKey("printedExpDate"))

                printedExpDate = (LocalDate) result.getSerializable("printedExpDate");

            // Get the foodItem from the bundle
            foodItem = (NestUPC) result.getSerializable("foodItem");

            assert foodItem != null : "Failed to retrieve required data";

            initializeMonthPicker(view);

            initializeDayPicker(view);

            initializeYearPicker(view);

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        //////////////////////////////// On Accept Button Pressed   ////////////////////////////////

        view.findViewById(R.id.accept_btn).setOnClickListener(clickedView -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate);

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            // Navigate to the proper fragment
            NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                    .navigate(R.id.action_CED_SelectPrintedExpirationDateFragment_to_StatusFragment);

        });

        ///////////////////////////////// On Back Button Pressed   /////////////////////////////////

        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                Bundle result = new Bundle();

                result.putSerializable("foodItem", foodItem);

                getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            }

            // Always return false since we aren't handling the navigation here.
            return false;

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a month, and
     * sets its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeMonthPicker(@NonNull View view) {

        NumberPicker monthPicker = view.findViewById(R.id.number_picker_month);

        // Set the min and max values to be 0-based
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);

        // Get an array of the months as strings and set it as the displayed values
        monthPicker.setDisplayedValues(new DateFormatSymbols().getMonths());

        monthPicker.setValue(printedExpDate.getMonthValue());

        monthPicker.setOnValueChangedListener(this::onMonthChanged);

        view.findViewById(R.id.increment_month_btn).setOnClickListener(increment_month_btn -> {

            int oldVal = monthPicker.getValue();

            int newVal = oldVal < 12 ? oldVal + 1 : 1;

            monthPicker.setValue(newVal);

            onMonthChanged(monthPicker, oldVal, newVal);

        });

        view.findViewById(R.id.decrement_month_btn).setOnClickListener(decrement_month_btn -> {

            int oldVal = monthPicker.getValue();

            int newVal = oldVal > 1 ? oldVal - 1 : 12;

            monthPicker.setValue(newVal);

            onMonthChanged(monthPicker, oldVal, newVal);

        });

    }

    /**
     * Called when the month is changed by the user.
     * @param picker The {@link NumberPicker} that was changed.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void onMonthChanged(NumberPicker picker, int oldVal, int newVal) {

        printedExpDate = printedExpDate.withMonth(newVal);

        // Make sure we update the number of days in the selected month
        dayPicker.setMaxValue(printedExpDate.lengthOfMonth());

        dayPicker.setValue(printedExpDate.getDayOfMonth());

    }

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a day of the
     * selected month, and sets its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeDayPicker(@NonNull View view) {

        dayPicker = view.findViewById(R.id.number_picker_day);

        dayPicker.setMinValue(1);

        // Set the max value to the actual number of days in the current month
        dayPicker.setMaxValue(printedExpDate.lengthOfMonth());

        dayPicker.setValue(printedExpDate.getDayOfMonth());

        dayPicker.setOnValueChangedListener(this::onDayChanged);

        view.findViewById(R.id.increment_day_btn).setOnClickListener(increment_day_btn -> {

            int oldVal = dayPicker.getValue();

            int newVal = oldVal < printedExpDate.lengthOfMonth() ? oldVal + 1 : 1;

            dayPicker.setValue(newVal);

            onDayChanged(dayPicker, oldVal, newVal);

        });

        view.findViewById(R.id.decrement_day_btn).setOnClickListener(decrement_day_btn -> {

            int oldVal = dayPicker.getValue();

            int newVal = oldVal > 1 ? oldVal - 1 : printedExpDate.lengthOfMonth();

            dayPicker.setValue(newVal);

            onDayChanged(dayPicker, oldVal, newVal);

        });

    }

    /**
     * Called when the day is changed by the user.
     * @param picker The {@link NumberPicker} that was changed.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void onDayChanged(NumberPicker picker, int oldVal, int newVal) {

        printedExpDate = printedExpDate.withDayOfMonth(newVal);

    }

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a year, and sets
     * its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeYearPicker(@NonNull View view) {

        NumberPicker yearPicker = view.findViewById(R.id.number_picker_year);

        final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
        final int MIN_YEAR = CURRENT_YEAR - 10;
        final int MAX_YEAR = CURRENT_YEAR + 10;

        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);

        yearPicker.setValue(printedExpDate.getYear());

        yearPicker.setOnValueChangedListener(this::onYearChanged);

        view.findViewById(R.id.increment_year_btn).setOnClickListener(increment_year_btn -> {

            int oldVal = yearPicker.getValue();

            int newVal = oldVal < MAX_YEAR ? oldVal + 1 : MIN_YEAR;

            yearPicker.setValue(newVal);

            onYearChanged(yearPicker, oldVal, newVal);

        });

        view.findViewById(R.id.decrement_year_btn).setOnClickListener(decrement_year_btn -> {

            int oldVal = yearPicker.getValue();

            int newVal = oldVal > MIN_YEAR ? oldVal - 1 : MAX_YEAR;

            yearPicker.setValue(newVal);

            onYearChanged(yearPicker, oldVal, newVal);

        });

    }

    /**
     * Called when the year is changed by the user.
     * @param picker The {@link NumberPicker} that was changed.
     * @param oldVal The old value.
     * @param newVal The new value.
     */
    private void onYearChanged(NumberPicker picker, int oldVal, int newVal) {

        printedExpDate = printedExpDate.withYear(newVal);

        // Make sure we update the number of days in the month, in case of leap years
        dayPicker.setMaxValue(printedExpDate.lengthOfMonth());

        dayPicker.setValue(printedExpDate.getDayOfMonth());

    }

}