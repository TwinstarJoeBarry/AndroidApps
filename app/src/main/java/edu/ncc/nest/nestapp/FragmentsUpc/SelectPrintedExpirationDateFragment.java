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
import android.widget.EditText;
import android.widget.PopupMenu;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.w3c.dom.Text;

import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class SelectPrintedExpirationDateFragment extends Fragment
{
    private NestUPC item;
    private int monthNum, dayNum, yearNum;
    private String selectedDate, actualDate;
    private TextView monthText, dayText, yearText;
    private Button monthBtn, dayBtn, yearBtn;

    /* days in the index of each month, add one to index */
    private final int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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

//        dayBtn.setOnClickListener( v -> pickDay();
        monthBtn.setOnClickListener( v -> pickMonth() );
//        yearBtn.setOnClickListener( v -> pickYear() );
    }


    /**
     * pickMonth()
     * DISPLAY MONTH PICKER
     **/
    private void pickMonth()
    {
        // SHOW THE POP UP MENU FOR THE MAIN CATEGORIES, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), monthBtn);
        Menu menu = menuPop.getMenu();

        String[] mainCategories = getResources().getStringArray(R.array.MONTHS);

        for (int i = 0; i < mainCategories.length; ++i)
            menu.add(daysInMonth[i], i + 1, i + 1, mainCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item ->
        {
            // set a text view with the category for the user to see;
            monthText.setText(item.toString());
            monthNum = item.getItemId();

//            // clear out subcategory between menu changes;
//            monthText.setText(" ");
//            subCategory =  -1;
            return true;
        });
        
        menuPop.show();
    }

}