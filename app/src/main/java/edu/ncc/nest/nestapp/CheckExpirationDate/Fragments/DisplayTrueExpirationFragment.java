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
import android.view.View;
import android.view.ViewGroup;
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

    private NestDBDataSource dataSource;

    private ShelfLife shortestShelfLife;

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
                this, (requestKey, data) -> {

            // Retrieve the NestUPC from the bundle
            foodItem = (NestUPC) data.getSerializable("foodItem");

            // Retrieve the printed expiration date from the bundle
            printedExpDate.setTime((Date) data.getSerializable("printedExpDate"));

            // Display item name, upc, category name on fragment_check_expiration_date_display_true_expiration.xml
            ((TextView) view.findViewById(R.id.item_display)).setText(foodItem.getProductName());
            ((TextView) view.findViewById(R.id.upc_display)).setText(foodItem.getUpc());
            ((TextView) view.findViewById(R.id.category_display)).setText(foodItem.getCatDesc());
            ((TextView) view.findViewById(R.id.printed_exp_date)).setText(
                    new SimpleDateFormat("MM/dd/yyyy",
                            Locale.getDefault()).format(printedExpDate.getTime()));

            // Retrieve a reference to the database from this fragment's activity
            dataSource = CheckExpirationDateActivity.requireDataSource(this);

            // Get the product's shelf lives from the database and calculate the shortest shelf life
            shortestShelfLife = getShortestShelfLife(
                    dataSource.getShelfLivesForProduct(foodItem.getProductId()));

            // Calculate and display the food item's true expiration date to the user
            ((TextView) view.findViewById(R.id.true_exp_date))
                    .setText(calculateTrueExpDate(shortestShelfLife));

            ((TextView) view.findViewById(R.id.storage_type)).setText(
                    formatString(shortestShelfLife.getDesc()));

            ((TextView) view.findViewById(R.id.storage_tips)).setText(
                    shortestShelfLife.getTips() != null ?
                            formatString(shortestShelfLife.getTips()) : "N/A");

            // Clear the FragmentResultListener so we can use this requestKey again.
            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Formats a String so that the first letter is capitalized and that it is ensured to end with a
     * period.
     * @param s The String to format
     * @return The formatted String
     */
    private String formatString(String s) {

        if (s != null && !s.isEmpty()) {

            s =  String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1).toLowerCase();

            return !s.endsWith(".") ? s + "." : s;

        } else

            return s;

    }

    /**
     * Finds and returns the shortest shelf life from a List of {@link ShelfLife} objects.
     *
     * @param shelfLives A list of {@link ShelfLife} objects.
     * @return The shortest shelf life from the list.
     */
    public ShelfLife getShortestShelfLife(List<ShelfLife> shelfLives) {

        int index = -1;

        String metric = "";

        ShelfLife shelfLife;

        // loop through the list of shelf lives and compare its metric
        for (int i = 0; i < shelfLives.size(); i++) {

            shelfLife = shelfLives.get(i);

            switch (shelfLife.getMetric()) {

                case "Years":
                    if (metric.isEmpty()) {
                        metric = "Years";
                        index = i;
                    }
                    break;

                case "Months":
                    if (metric.isEmpty() || metric.equals("Years")) {
                        metric = "Months";
                        index = i;
                    }
                    break;

                case "Weeks":
                    if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months")) {
                        metric = "Weeks";
                        index = i;
                    }
                    break;

                case "Days":
                    if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months") || metric.equals("Weeks")) {
                        metric = "Days";
                        index = i;
                    }
                    break;

                default:
                    Log.d(LOG_TAG, "getShortestShelfLife: Error invalid option");
                    break;

            }

        }

        return shelfLives.get(index);

    }

    /**
     * Calculates the true expiration date of an item based on it's shelf life and printed
     * expiration date.
     * @param shelfLife The shelf life of the item.
     */
    public String calculateTrueExpDate(ShelfLife shelfLife) {

        Log.d(LOG_TAG, "Metric: " + shelfLife.getMetric() + ", Min: " + shelfLife.getMin() +
                ", Max: " + shelfLife.getMax());

        // Get an instance of Calendar
        Calendar c = Calendar.getInstance();

        // Update the time to the printed expiration date
        c.setTime(printedExpDate.getTime());

        switch (shelfLife.getMetric().toLowerCase()) {

            case "days":

                // Add max number of days to the printed expiration date
                c.add(Calendar.DAY_OF_MONTH, shelfLife.getMax());

                break;

            case "weeks":

                // Add max number of weeks to the printed expiration date
                c.add(Calendar.WEEK_OF_MONTH, shelfLife.getMax());

                break;

            case "months":

                // Add max number of months to the printed expiration date
                c.add(Calendar.MONTH, shelfLife.getMax());

                break;

            case "years":

                // Add max number of years to the printed expiration date
                c.add(Calendar.YEAR, shelfLife.getMax());

                break;

        }

        // Format the date to the pattern of MM/dd/yyyy and return the result
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(c.getTime());

    }

}