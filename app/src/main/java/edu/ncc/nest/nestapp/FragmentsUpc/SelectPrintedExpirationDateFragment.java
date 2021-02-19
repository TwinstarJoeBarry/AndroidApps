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
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.PopupMenu;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class SelectPrintedExpirationDateFragment extends Fragment
{
    private final int STARTING_YEAR = 2020;
    private final int ADDITIONAL_YEARS = 10;

    private NestUPC item;
    private int monthNum, dayNum, yearNum;
    private String selectedDate, actualDate;
    private TextView monthText, dayText, yearText;
    private Button monthBtn, dayBtn, yearBtn;

    /* days in the index of each month, add one to index */
    private final int daysInMonth[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        NestUPC item;
        monthNum = dayNum = yearNum = -1;
        selectedDate = actualDate = "NOT YET PARSED";
        return inflater.inflate(R.layout.fragment_select_printed_expiration_date, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // INITIALIZE VARIABLES;
        dayText = view.findViewById(R.id.selected_print_day_text);
        monthText = view.findViewById(R.id.selected_print_month_text);
        yearText = view.findViewById(R.id.selected_print_year_text);

        dayBtn = view.findViewById(R.id.selected_print_day_button);
        monthBtn = view.findViewById(R.id.selected_print_month_button);
        yearBtn = view.findViewById(R.id.selected_print_year_button);

        dayBtn.setOnClickListener( v -> pickDay() );
        monthBtn.setOnClickListener( v -> pickMonth() );
        yearBtn.setOnClickListener( v -> pickYear() );

        // GRAB THE UPC VALUES FROM THE BUNDLE SENT FROM SCAN FRAG OR CONFIRM UPC FRAG WHEN READY
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM", this, (key, bundle) ->
        {

            item = (NestUPC)bundle.getSerializable("foodItem"); // TODO this probably won't work if there isn't a UPC to get

            assert item != null : "Item equals null";

            ((TextView)view.findViewById(R.id.selected_print_headline)).setText( item.getUpc() );

            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        // ACCEPT BUTTON CODE - PARSE VALUES FOR NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.selected_print_accept).setOnClickListener( view1 ->
        {
            if (monthNum == -1 || dayNum == -1 || yearNum == -1)
                Toast.makeText(getContext(), "Please select a full date!", Toast.LENGTH_LONG).show();

            else
            {
                // retrieve the String information from each view, casting as appropriate;
                String fullDate = ("" + monthNum + "/" + dayNum + "/" + yearNum);
                Toast.makeText(getContext(), fullDate, Toast.LENGTH_LONG).show();

                Bundle saved = new Bundle();

                saved.putString("DATE", fullDate);

                saved.putSerializable("foodItem", item);

                // Need to clear the listener with the same request key, before using same request key again.
                getParentFragmentManager().clearFragmentResult("FOOD ITEM");

                // Set the fragment result to the bundle
                getParentFragmentManager().setFragmentResult("FOOD ITEM", saved);

                // navigate over to the proper fragment
                NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                        .navigate(R.id.action_selectPrintedExpirationDateFragment_to_displayTrueExpirationFragment);
            }
        });
    }


    /**
     * pickMonth()
     * DISPLAY MONTH PICKER
     **/
    private void pickMonth()
    {
        // SHOW THE POP UP MENU FOR THE MONTHS, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), monthBtn);
        Menu menu = menuPop.getMenu();

        String[] mainCategories = getResources().getStringArray(R.array.MONTHS);

        for (int i = 0; i < mainCategories.length; ++i)
            menu.add(daysInMonth[i], i + 1, i + 1, mainCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE MONTH INDEX
        menuPop.setOnMenuItemClickListener(item ->
        {
            // set a text view with the month for the user to see;
            monthText.setText(item.toString());
            monthNum = item.getItemId();

            // clear out day category between menu changes;
            dayText.setText(" ");
            dayNum =  -1;
            return true;
        });

        menuPop.show();
    }


    /**
     * pickDay()
     * DISPLAY DAY PICKER
     **/
    private void pickDay()
    {
        // SHOW THE POP UP MENU FOR THE DAY, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), dayBtn);
        Menu menu = menuPop.getMenu();

        int daysThisMonth = daysInMonth[monthNum];
        for (int i = 1; i <= daysThisMonth;  ++i )
            menu.add(i, i, i, "" + i);

        // THE ACTUAL ON CLICK CODE TO SET THE DAY INDEX
        menuPop.setOnMenuItemClickListener(item ->
        {
            // set a text view with the day for the user to see;
            dayText.setText(item.toString());
            dayNum = item.getItemId();
            return true;
        });

        menuPop.show();
    }


    /**
     * pickYear()
     * DISPLAY YEAR PICKER
     **/
    private void pickYear()
    {
        // SHOW THE POP UP MENU FOR THE YEAR, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), yearBtn);
        Menu menu = menuPop.getMenu();

        int endingYear = STARTING_YEAR + ADDITIONAL_YEARS;
        for (int i = STARTING_YEAR; i <= endingYear;  ++i )
            menu.add(i, i, i, "" + i);

        // THE ACTUAL ON CLICK CODE TO SET THE DAY INDEX
        menuPop.setOnMenuItemClickListener(item ->
        {
            // set a text view with the day for the user to see;
            yearText.setText(item.toString());
            yearNum = item.getItemId();
            return true;
        });

        menuPop.show();
    }

}