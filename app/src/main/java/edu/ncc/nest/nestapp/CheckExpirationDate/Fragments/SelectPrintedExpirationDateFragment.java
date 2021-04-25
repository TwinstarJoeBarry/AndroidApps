package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectPrintedExpirationDateFragment: Allows the user to select or enter the item's printed
 * expiration date, then sends the result to {@link DisplayTrueExpirationFragment}.
 */
public class SelectPrintedExpirationDateFragment extends Fragment {

    // Number of days in the respective month. (Index 0)=Jan, (Index 1)=Feb, ...
    private static final int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private static final int STARTING_YEAR = 2020;
    private static final int ADDITIONAL_YEARS = 10;

    private NestUPC foodItem;
    private int monthNum, dayNum, yearNum;
    private TextView monthText, dayText, yearText;
    private String iUPC;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        monthNum = dayNum = yearNum = -1;

        return inflater.inflate(R.layout.fragment_check_expiration_date_select_printed_expiration_date,
                container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference to each of the TextViews from the current View
        monthText = view.findViewById(R.id.selected_print_month_text);
        dayText = view.findViewById(R.id.selected_print_day_text);
        yearText = view.findViewById(R.id.selected_print_year_text);

        // Get a reference to each of the Buttons from the current View and set their OnClick methods to call the respective method
        view.findViewById(R.id.selected_print_month_button).setOnClickListener(this::pickMonth);
        view.findViewById(R.id.selected_print_day_button).setOnClickListener(this::pickDay);
        view.findViewById(R.id.selected_print_year_button).setOnClickListener(this::pickYear);

        // GRAB THE UPC VALUES FROM THE BUNDLE SENT FROM SCAN FRAG OR CONFIRM UPC FRAG WHEN READY
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM", this, (key, bundle) -> {

            // Get the foodItem from the bundle
            foodItem = (NestUPC) bundle.getSerializable("foodItem"); // TODO this probably won't work if there isn't a UPC to get

            iUPC = foodItem.getUpc();

            if (foodItem != null)

                ((TextView) view.findViewById(R.id.selected_print_headline)).setText( iUPC );

            // Make sure we clear the FragmentResultListener so we can use this requestKey again
            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        // Set the OnClickListener for the "Accept" Button
        view.findViewById(R.id.selected_print_accept).setOnClickListener( view1 -> {

            if (monthNum == -1 || dayNum == -1 || yearNum == -1)

                Toast.makeText(getContext(), "Please select a full date!", Toast.LENGTH_LONG).show();

            else {

                // Create a String containing the full date
                String fullDate = (monthNum + "/" + dayNum + "/" + yearNum);

                Toast.makeText(getContext(), fullDate, Toast.LENGTH_LONG).show();

                Bundle saved = new Bundle();

                saved.putSerializable("foodItem", foodItem);

                saved.putString("PRINTED EXPIRATION DATE", fullDate);

                // Make sure we clear the FragmentResult so we can re-use the requestKey
                getParentFragmentManager().clearFragmentResult("FOOD ITEM");

                // Set the fragment result to the bundle
                getParentFragmentManager().setFragmentResult("FOOD ITEM", saved);

                // Navigate to the proper fragment
                NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                        .navigate(R.id.action_CED_SelectPrintedExpirationDateFragment_to_DisplayTrueExpirationFragment);

            }

        });

    }


    /**
     * pickMonth --
     * Creates and shows a PopupMenu that allows the user to select a specific month.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickMonth(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        String[] months = getResources().getStringArray(R.array.months);

        // Add each of the months to the menu
        for (int i = 0; i < months.length; ++i)

            menu.add(daysInMonth[i], i + 1, i + 1, months[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE MONTH INDEX
        menuPop.setOnMenuItemClickListener(item -> {

            // Set a the text of the TextView to the display the month the user selected
            monthText.setText(item.toString());

            monthNum = item.getItemId();

            // Reset the selected day to unselected when the month changes
            dayText.setText(R.string.fragment_ced_select_printed_expiration_date_day_lbl);

            dayNum =  -1;

            return true;

        });

        menuPop.show();

    }


    /**
     * pickDay --
     * Creates and shows a PopupMenu that allows the user to select a specific day.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickDay(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        int daysInMonth = this.daysInMonth[monthNum - 1];

        // Add each day to the menu
        for (int i = 1; i <= daysInMonth;  ++i )

            menu.add(i, i, i, "" + i);

        // THE ACTUAL ON CLICK CODE TO SET THE DAY INDEX
        menuPop.setOnMenuItemClickListener(item -> {

            // Set a the text of the TextView to the display the month the user selected
            dayText.setText(item.toString());

            dayNum = item.getItemId();

            return true;

        });

        menuPop.show();

    }


    /**
     * pickYear --
     * Creates and shows a PopupMenu that allows the user to select a specific day.
     * @param anchorView The view to anchor the PopupMenu to.
     **/
    private void pickYear(View anchorView) {

        PopupMenu menuPop = new PopupMenu(getContext(), anchorView);

        Menu menu = menuPop.getMenu();

        int endingYear = STARTING_YEAR + ADDITIONAL_YEARS;

        // Add each year between the starting and end year inclusive
        for (int i = STARTING_YEAR; i <= endingYear;  ++i)

            menu.add(i, i, i, "" + i);

        // THE ACTUAL ON CLICK CODE TO SET THE DAY INDEX
        menuPop.setOnMenuItemClickListener(item -> {

            // Set a the text of the TextView to the display the year the user selected
            yearText.setText(item.toString());

            yearNum = item.getItemId();

            return true;
        });

        menuPop.show();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("*****", "onSaveInstanceState");

        Bundle bundle = new Bundle();

        bundle.putString("barcode", iUPC);

        // Need to clear the result with the same request key, before using possibly same request key again.
        getParentFragmentManager().clearFragmentResult("BARCODE");

        getParentFragmentManager().setFragmentResult("BARCODE", bundle);
    }

}