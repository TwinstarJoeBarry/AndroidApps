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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectPrintedExpirationDateFragment: Allows the user to select or enter the item's printed
 * expiration date, then sends the result to {@link DisplayTrueExpirationFragment}.
 */
public class SelectPrintedExpirationDateFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = SelectPrintedExpirationDateFragment.class.getSimpleName();

    private static final String[] monthNames = new DateFormatSymbols().getMonths();

    private static final int ADDITIONAL_YEARS = 10;

    private final Calendar printedExpDate = Calendar.getInstance();

    private TextView monthTextView, dayTextView, yearTextView;

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

        // Get a reference to each of the TextViews from the current View
        monthTextView = view.findViewById(R.id.selected_print_month_text);
        dayTextView = view.findViewById(R.id.selected_print_day_text);
        yearTextView = view.findViewById(R.id.selected_print_year_text);

        // Update the printed expiration date to reflect the current date
        setPrintedExpDate(printedExpDate.get(Calendar.YEAR), printedExpDate.get(Calendar.MONTH),
                printedExpDate.get(Calendar.DAY_OF_MONTH));

        /* Get a reference to each of the Buttons from the current View and set their OnClick
           methods to call the respective method */
        view.findViewById(R.id.selected_print_month_button).setOnClickListener(this::pickMonth);
        view.findViewById(R.id.selected_print_day_button).setOnClickListener(this::pickDay);
        view.findViewById(R.id.selected_print_year_button).setOnClickListener(this::pickYear);

        // Listen for the foodItem from the bundle sent from the previous fragment
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            /* This request key is only required when navigating back from
             * DisplayTrueExpirationFragment */
            if (result.containsKey("printedExpDate"))

                // Retrieve the printed expiration date from the bundle
                printedExpDate.setTime((Date) result.getSerializable("printedExpDate"));

            // Get the foodItem from the bundle
            foodItem = (NestUPC) result.getSerializable("foodItem");

            assert foodItem != null : "Failed to retrieve required data";

            ((TextView) view.findViewById(R.id.selected_print_headline))
                    .setText(foodItem.getUpc());

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        // Set the OnClickListener for the "Accept" Button
        view.findViewById(R.id.selected_print_accept).setOnClickListener(clickedView -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate.getTime());

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            // Navigate to the proper fragment
            NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                    .navigate(R.id.action_CED_SelectPrintedExpirationDateFragment_to_DisplayTrueExpirationFragment);

        });

        //////////////////////////////// On Back Button Pressed   //////////////////////////////////

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
     * Creates and shows a PopupMenu that allows the user to select a specific month.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickMonth(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        // Add each of the months to the menu
        for (int i = 0; i < monthNames.length; i++)

            menu.add(Menu.NONE, i, Menu.NONE, monthNames[i]);

        menuPop.setOnMenuItemClickListener(item -> {

            setPrintedExpDate(printedExpDate.get(Calendar.YEAR), item.getItemId(),
                    printedExpDate.get(Calendar.DAY_OF_MONTH));

            return true;

        });

        menuPop.show();

    }


    /**
     * Creates and shows a PopupMenu that allows the user to select a specific day.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickDay(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        final int ACTUAL_MAXIMUM = printedExpDate.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add each day of the month to the menu
        for (int i = 1; i <= ACTUAL_MAXIMUM; i++)

            menu.add(Menu.NONE, i, Menu.NONE, String.valueOf(i));

        menuPop.setOnMenuItemClickListener(item -> {

            setPrintedExpDate(printedExpDate.get(Calendar.YEAR), printedExpDate.get(Calendar.MONTH),
                    item.getItemId());

            return true;

        });

        menuPop.show();

    }


    /**
     * Creates and shows a PopupMenu that allows the user to select a specific year.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickYear(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

        // Add each year between the current and end year inclusive
        for (int i = 0; i <= ADDITIONAL_YEARS; i++)

            menu.add(Menu.NONE, CURRENT_YEAR + i, Menu.NONE,
                    String.valueOf(CURRENT_YEAR + i));

        menuPop.setOnMenuItemClickListener(item -> {

            setPrintedExpDate(item.getItemId(), printedExpDate.get(Calendar.MONTH),
                    printedExpDate.get(Calendar.DAY_OF_MONTH));

            return true;

        });

        menuPop.show();

    }

    /**
     * Set the printed expiration date to the desired date.
     * @param year The desired year
     * @param month The desired month
     * @param day The desired day
     */
    private void setPrintedExpDate(int year, int month, int day) {

        // Clamp and set the date
        printedExpDate.setTime(clampDate(year, month, day));

        // Update TextViews to display the new date
        monthTextView.setText(monthNames[printedExpDate.get(Calendar.MONTH)]);

        dayTextView.setText(String.valueOf(printedExpDate.get(Calendar.DAY_OF_MONTH)));

        yearTextView.setText(String.valueOf(printedExpDate.get(Calendar.YEAR)));

    }

    /**
     * Clamps a date so that the days can't be greater than the actual maximum number of days in
     * the month of the year.
     * @param year The year
     * @param month The month
     * @param day The day
     * @return A Date object
     */
    private Date clampDate(int year, int month, int day) {

        Calendar temp = Calendar.getInstance();

        // Clear all fields to make sure only the date fields get set
        temp.clear();

        // Make sure to set the year and month before calculating the actual maximum number of days
        temp.set(year, month, 1);

        // Set and clamp the date to the actual maximum number of days
        temp.set(year, month, Math.min(day, temp.getActualMaximum(Calendar.DAY_OF_MONTH)));

        return temp.getTime();

    }

}