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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
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

    private LocalDate printedExpDate = LocalDate.now();

    private NestDBDataSource dataSource;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve a reference to the database from this fragment's activity
        dataSource = CheckExpirationDateActivity.requireDataSource(this);

    }

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

            printedExpDate = (LocalDate) result.getSerializable("printedExpDate");

                    assert foodItem != null && printedExpDate != null : "Failed to retrieve required data";

            // Display item information
            ((TextView) view.findViewById(R.id.item)).setText(foodItem.getProductName());

            ((TextView) view.findViewById(R.id.upc)).setText(foodItem.getUpc());

            ((TextView) view.findViewById(R.id.category)).setText(foodItem.getCatDesc());

            ((TextView) view.findViewById(R.id.type)).setText(foodItem.getProductSubtitle());

            ((TextView) view.findViewById(R.id.printed_exp_date)).setText(
                    dateTimeFormatter.format(printedExpDate));

            // Get the product's shelf lives from the database and calculate the shortest shelf life
            ShelfLife dop_pantryLife =
                    dataSource.getItemShelfLife(foodItem.getProductId(), ShelfLife.DOP_PL);

            // Calculate and display shelf life data
            ((TextView) view.findViewById(R.id.shelf_life))
                    .setText(getShelfLifeRange(dop_pantryLife));

            ((TextView) view.findViewById(R.id.storage_type))
                    .setText(dop_pantryLife.getDesc());

            ((TextView) view.findViewById(R.id.storage_tips))
                    .setText(dop_pantryLife.getTips() != null ? dop_pantryLife.getTips() : "N/A");

            // Calculate and display the food item's true expiration date to the user
            ((TextView) view.findViewById(R.id.true_exp_date))
                    .setText(calculateTrueExpDateRange(dop_pantryLife));

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        //////////////////////////////// On Back Button Pressed   //////////////////////////////////

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
     * Finds and returns the shortest shelf life from a List of {@link ShelfLife} objects.
     *
     * @param shelfLives A list of {@link ShelfLife} objects.
     * @return The shortest shelf life from the list.
     *
     * @deprecated This method is deprecated since are now only displaying dop_pantryLife.
     */
    @Deprecated
    public ShelfLife getShortestShelfLife(List<ShelfLife> shelfLives) {

        int index = -1;

        String metric = "";

        ShelfLife shelfLife;

        // loop through the list of shelf lives and compare its metric
        for (int i = 0; i < shelfLives.size(); i++) {

            shelfLife = shelfLives.get(i);

            if (shelfLife.getMetric() != null)

                switch (shelfLife.getMetric()) {

                    case "Indefinitely":
                        if (metric.isEmpty()) {
                            metric = "Indefinitely";
                            index = i;
                        }
                        break;

                    case "Years":
                        if (metric.isEmpty() || metric.equals("Indefinitely")) {
                            metric = "Years";
                            index = i;
                        }
                        break;

                    case "Months":
                        if (metric.isEmpty() || metric.equals("Years") || metric.equals("Indefinitely")) {
                            metric = "Months";
                            index = i;
                        }
                        break;

                    case "Weeks":
                        if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months") || metric.equals("Indefinitely")) {
                            metric = "Weeks";
                            index = i;
                        }
                        break;

                    case "Days":
                        if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months") || metric.equals("Weeks") || metric.equals("Indefinitely")) {
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
     * Calculates the true expiration date range of an item based on it's shelf life and printed
     * expiration date.
     * @param shelfLife The shelf life of a item.
     */
    public String calculateTrueExpDateRange(ShelfLife shelfLife) {

        String shelfLifeMetric = shelfLife.getMetric();

        if (shelfLifeMetric == null)

            return "N/A";

        shelfLifeMetric = shelfLifeMetric.toUpperCase();

        switch (shelfLifeMetric) {

            case "DAYS": case "WEEKS": case "MONTHS": case "YEARS":

                ChronoUnit chronoUnit = ChronoUnit.valueOf(shelfLifeMetric);

                // Get two instances of the Calendar class
                LocalDate min = printedExpDate.plus(shelfLife.getMin(), chronoUnit);
                LocalDate max = printedExpDate.plus(shelfLife.getMax(), chronoUnit);

                if (min.compareTo(max) == 0)

                    return dateTimeFormatter.format(max);

                return (dateTimeFormatter.format(min) + " - " + dateTimeFormatter.format(max));

            default:

                throw new RuntimeException("Missing case for shelf life metric: " +
                        shelfLifeMetric);

            case "indefinitely":

                return "Indefinite";

        }

    }

    /**
     * Returns a string containing the shelf life range of a {@code ShelfLife} object.
     * @param shelfLife The shelf life of a item.
     */
    public String getShelfLifeRange(ShelfLife shelfLife) {

        if (shelfLife != null && shelfLife.getMetric() != null) {

            String shelfLifeMetric = shelfLife.getMetric();

            if (shelfLifeMetric.equals("Indefinitely"))

                return "Indefinite";

            int min = shelfLife.getMin(), max = shelfLife.getMax();

            if (min == max) {

                if (max == 1)

                    return max + " " + shelfLifeMetric.substring(0, shelfLifeMetric.length() - 1);

                return max + " " + shelfLifeMetric;

            } else

                return min + " - " + max + " " + shelfLifeMetric;

        } else

            return "N/A";

    }

}