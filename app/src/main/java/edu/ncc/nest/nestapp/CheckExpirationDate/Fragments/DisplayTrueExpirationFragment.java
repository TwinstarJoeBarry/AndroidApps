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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

/**
 * DisplayTrueExpirationFragment:  Calculates the true expiration date based upon the printed
 * expiration date. Then displays the true expiration date for the item along with a synopsis of
 * the item including the UPC, category, item, and printed expiration date.
 */
public class DisplayTrueExpirationFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    private static final String LOG_TAG = DisplayTrueExpirationFragment.class.getSimpleName();

    private final Calendar printedExpDate = Calendar.getInstance();

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_display_true_expiration,
                container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the FragmentResultListener
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            // Retrieve the NestUPC from the bundle
            foodItem = (NestUPC) result.getSerializable("foodItem");

            Date printedExpDate = (Date) result.getSerializable("printedExpDate");

            assert foodItem != null && printedExpDate != null : "Failed to retrieve required data";

            // Update the printed expiration date to the retrieve date
            this.printedExpDate.setTime(printedExpDate);

            // Display item information
            ((TextView) view.findViewById(R.id.item)).setText(foodItem.getProductName());

            ((TextView) view.findViewById(R.id.upc)).setText(foodItem.getUpc());

            ((TextView) view.findViewById(R.id.category)).setText(foodItem.getCatDesc());

            ((TextView) view.findViewById(R.id.type)).setText(foodItem.getProductSubtitle());

            ((TextView) view.findViewById(R.id.brand)).setText(foodItem.getBrand());

            ((TextView) view.findViewById(R.id.description)).setText(foodItem.getDescription());

            ((TextView) view.findViewById(R.id.printed_exp_date)).setText(
                    new SimpleDateFormat("MM/dd/yyyy",
                            Locale.getDefault()).format(printedExpDate));

            // Retrieve a reference to the database from this fragment's activity
            NestDBDataSource dataSource =
                    CheckExpirationDateActivity.requireDataSource(this);

            List<ShelfLife> shelfLives =
                    dataSource.getShelfLivesForProduct(foodItem.getProductId());

            LinearLayout scrollLayout = view.findViewById(R.id.scroll_layout);

            for (ShelfLife shelfLife : shelfLives) {

                View shelfLifeView = getLayoutInflater()
                        .inflate(R.layout.list_item_shelf_life, scrollLayout, false);

                ((TextView) shelfLifeView.findViewById(R.id.shelf_life))
                        .setText(getShelfLifeRange(shelfLife));

                ((TextView) shelfLifeView.findViewById(R.id.storage_type))
                        .setText(shelfLife.getDesc());

                ((TextView) shelfLifeView.findViewById(R.id.storage_tips)).setText(
                        shelfLife.getTips() != null ? shelfLife.getTips() : "N/A");

                // Calculate and display the food item's true expiration date to the user
                ((TextView) shelfLifeView.findViewById(R.id.true_exp_date))
                        .setText(calculateTrueExpDateRange(shelfLife));

                scrollLayout.addView(shelfLifeView);

            }

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Calculates the true expiration date range of an item based on it's shelf life and printed
     * expiration date.
     * @param shelfLife The shelf life of a item.
     */
    public String calculateTrueExpDateRange(ShelfLife shelfLife) {

        // Get the printed expiration date as a Date object
        Date printedExpDate = this.printedExpDate.getTime();

        // Get two instances of the Calendar class
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        // Update their times to the printed expiration date
        min.setTime(printedExpDate);
        max.setTime(printedExpDate);

        switch (shelfLife.getMetric().toLowerCase()) {

            case "days":

                // Add min number of days to the printed expiration date
                min.add(Calendar.DAY_OF_MONTH, shelfLife.getMin());

                // Add max number of days to the printed expiration date
                max.add(Calendar.DAY_OF_MONTH, shelfLife.getMax());

                break;

            case "weeks":

                // Add min number of weeks to the printed expiration date
                min.add(Calendar.WEEK_OF_MONTH, shelfLife.getMin());

                // Add max number of weeks to the printed expiration date
                max.add(Calendar.WEEK_OF_MONTH, shelfLife.getMax());

                break;

            case "months":

                // Add min number of months to the printed expiration date
                min.add(Calendar.MONTH, shelfLife.getMin());

                // Add max number of months to the printed expiration date
                max.add(Calendar.MONTH, shelfLife.getMax());

                break;

            case "years":

                // Add min number of years to the printed expiration date
                min.add(Calendar.YEAR, shelfLife.getMin());

                // Add max number of years to the printed expiration date
                max.add(Calendar.YEAR, shelfLife.getMax());

                break;

        }

        // Format the date to the pattern of MM/dd/yyyy and return the result
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        if (min.compareTo(max) != 0)

            return sdf.format(max.getTime());

        return sdf.format(min.getTime()) + " - " + sdf.format(max.getTime());

    }

    /**
     * Returns a string containing the shelf life range of a {@code ShelfLife} object.
     * @param shelfLife The shelf life of a item.
     */
    public String getShelfLifeRange(ShelfLife shelfLife) {

        String metric = shelfLife.getMetric();

        if (shelfLife.getMin() == shelfLife.getMax())

            return shelfLife.getMax() + " " + metric;

        return shelfLife.getMin() + " - " + shelfLife.getMax() + " " + metric;

    }

}