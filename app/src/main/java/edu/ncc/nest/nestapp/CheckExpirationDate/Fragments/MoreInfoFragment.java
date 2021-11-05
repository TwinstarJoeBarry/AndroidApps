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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

/**
 * MoreInfoFragment:  Calculates the true expiration date based upon the printed
 * expiration date. Then displays the true expiration date for the item along with a synopsis of
 * the item including the UPC, category, item, and printed expiration date.
 */
public class MoreInfoFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    private static final String LOG_TAG = MoreInfoFragment.class.getSimpleName();

    public static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private LocalDate printedExpDate, trueExpDate;

    private ShelfLife pantryLife;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_more_info,
                container, false);


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the FragmentResultListener
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            // Retrieve the NestUPC from the bundle
            foodItem = (NestUPC) result.getSerializable("foodItem");

            pantryLife = (ShelfLife) result.getSerializable("pantryLife");

            trueExpDate = (LocalDate) result.getSerializable("trueExpDate");

            printedExpDate = (LocalDate) result.getSerializable("printedExpDate");

            assert foodItem != null && printedExpDate != null : "Failed to retrieve required data";

            // Display item information

            ((TextView) view.findViewById(R.id.item)).setText(foodItem.getProductName());

            ((TextView) view.findViewById(R.id.upc)).setText(foodItem.getUpc());

            ((TextView) view.findViewById(R.id.category)).setText(foodItem.getCatDesc());

            ((TextView) view.findViewById(R.id.type)).setText(foodItem.getProductSubtitle());

            ((TextView) view.findViewById(R.id.printed_exp_date)).setText(
                    dateTimeFormatter.format(printedExpDate));

            // Display pantry life information

            if (pantryLife != null) {

                ((TextView) view.findViewById(R.id.storage_type)).setText(pantryLife.getDesc());

                ((TextView) view.findViewById(R.id.storage_tips))
                        .setText(pantryLife.getTips() != null ? pantryLife.getTips() : "N/A");

            } else {

                ((TextView) view.findViewById(R.id.storage_type)).setText("N/A");

                ((TextView) view.findViewById(R.id.storage_tips)).setText("N/A");

            }

            // Display the food item's true expiration date to the user
            ((TextView) view.findViewById(R.id.true_exp_date)).setText(determineTrueExpDate());

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        ////////////////////////////////// On Back Button Pressed //////////////////////////////////

        view.findViewById(R.id.back_btn).setOnClickListener(back_btn -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate);

            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            NavHostFragment.findNavController(MoreInfoFragment.this)
                    .navigate(R.id.action_CED_MoreInfoFragment_to_StatusFragment);

        });

        ///////////////////////////// On System Back Button Pressed   //////////////////////////////

        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                Bundle bundle = new Bundle();

                bundle.putSerializable("foodItem", foodItem);

                bundle.putSerializable("printedExpDate", printedExpDate);

                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

            }

            // Always return false since we aren't handling the navigation here.
            return false;

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Determines the correct text to display for the true expiration date label.
     * @return {@code "Indefinite"}, {@code "Unknown}, or the true expiration date formatted using
     * {@code dateTimeFormatter}.
     */
    private String determineTrueExpDate() {

        if (trueExpDate != null) {

            if (trueExpDate == LocalDate.MAX)

                return "Indefinite";

            return dateTimeFormatter.format(trueExpDate);

        } else

            return "Unknown";

    }

}