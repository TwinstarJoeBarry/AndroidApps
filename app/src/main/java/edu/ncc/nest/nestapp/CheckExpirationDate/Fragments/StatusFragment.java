package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2021 The LibreFoodPantry Developers.
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

/**
 * Displays the status of the selected item's expiration using a gray, red, yellow, or green icon
 * that represents whether or not the item has truly expired.
 *
 * - Grey means the items shelf life is unknown/{@code null}
 * - Red means the item has expired and should be discarded.
 * - Yellow means the item is within 30 days of expiration.
 * - Green means the item is good for 30 days or more.
 */
public class StatusFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = StatusFragment.class.getSimpleName();

    public static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    private NestDBDataSource dataSource;

    private LocalDate printedExpDate;

    private LocalDate trueExpDate;

    private ShelfLife pantryLife;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve a reference to the database from this fragment's activity
        dataSource = CheckExpirationDateActivity.requireDataSource(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_status,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView statusMsg = view.findViewById(R.id.status_msg);

        final ImageView statusIcon = view.findViewById(R.id.status_icon);

        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            // Retrieve the food item from the bundle
            foodItem = (NestUPC) result.getSerializable("foodItem");

            // Retrieve the printed expiration date from the bundle
            printedExpDate = (LocalDate) result.getSerializable("printedExpDate");

            // Make sure we successfully retrieved the required data
            assert foodItem != null && printedExpDate != null : "Failed to retrieve required data";

            Log.d(LOG_TAG,
                    "Printed Expiration Date: " + dateTimeFormatter.format(printedExpDate));

            int productId = foodItem.getProductId();

            // Get the product's dop_pantryLife shelf life from the database
            pantryLife = dataSource.getItemShelfLife(productId, ShelfLife.DOP_PL);

            if (pantryLife == null)

                // Get the product's pantryLife shelf life from the database
                pantryLife = dataSource.getItemShelfLife(productId, ShelfLife.PL);

            // If pantryLife is non-null and has a valid metric
            if (validatePantryLife(view.findViewById(R.id.storage_tips))) {

                ((TextView) view.findViewById(R.id.storage_tips)).setText(
                        pantryLife.getTips() != null ? pantryLife.getTips() : "N/A");

                trueExpDate = calculateTrueExpDate(pantryLife);

                if (trueExpDate.equals(LocalDate.MAX)) {

                    Log.d(LOG_TAG, "True Expiration Date: Indefinite");

                    ((TextView) view.findViewById(R.id.true_exp_date)).setText(R.string.indefinite);

                    statusMsg.setText(R.string.status_fragment_indefinite_msg);

                    statusIcon.setImageResource(R.drawable.ic_indefinite);

                } else {

                    Log.d(LOG_TAG, "True Expiration Date: " +
                            dateTimeFormatter.format(trueExpDate));

                    ((TextView) view.findViewById(R.id.true_exp_date)).setText(
                            dateTimeFormatter.format(trueExpDate));

                    long numDays = LocalDate.now().until(trueExpDate, ChronoUnit.DAYS);

                    if (numDays <= 0) {

                        statusMsg.setText(R.string.status_fragment_discard_msg);

                        statusIcon.setImageResource(R.drawable.ic_delete);

                    } else if (numDays < 30) {

                        statusMsg.setText(R.string.status_fragment_warning_msg);

                        statusIcon.setImageResource(R.drawable.ic_warning);

                    } else {

                        statusMsg.setText(R.string.status_fragment_safe_msg);

                        statusIcon.setImageResource(R.drawable.ic_check_mark);

                    }

                }

            } else {

                Log.w(LOG_TAG, "True Expiration Date: Unknown");

                ((TextView) view.findViewById(R.id.true_exp_date)).setText(R.string.unknown);

                statusMsg.setText(R.string.status_fragment_unknown_msg);

                statusIcon.setImageResource(R.drawable.ic_help);

            }

            // Make sure to clear the listener so we can use the same request key in other fragments
            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        view.findViewById(R.id.select_new_date_btn).setOnClickListener(select_new_date_btn -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate);

            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            NavHostFragment.findNavController(StatusFragment.this)
                    .navigate(R.id.action_CED_StatusFragment_to_SelectPrintedExpirationDateFragment);

        });

        view.findViewById(R.id.more_info_btn).setOnClickListener(more_info_btn -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("pantryLife", pantryLife);

            result.putSerializable("trueExpDate", trueExpDate);

            result.putSerializable("printedExpDate", printedExpDate);

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            // Navigate to the proper fragment
            NavHostFragment.findNavController(StatusFragment.this)
                    .navigate(R.id.action_CED_StatusFragment_to_MoreInfoFragment);

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
     * Returns whether or not {@code pantryLife} is valid (Non-null, and has a non-null metric).
     * Displays any available storage tips the pantry life has in the given {@link TextView}.
     * @param storageTips The {@link TextView} to display any available storage tips in.
     * @return True if {@code pantryLife} is non-null and has a non-null metric, false otherwise.
     */
    private boolean validatePantryLife(@NonNull TextView storageTips) {

        if (pantryLife != null) {

            // Display any available storage tips
            storageTips.setText(pantryLife.getTips() != null ? pantryLife.getTips() : "N/A");

            return pantryLife.getMetric() != null;

        } else

            return false;

    }

    /**
     * Calculates the true expiration date of an item based on it's shelf life and printed
     * expiration date. ({@code printedExpDate} + {@link ShelfLife#getMax()} - 1 Month).
     * @param shelfLife The {@link ShelfLife} to use when calculate the date.
     * @return The true expiration date of the given item.
     */
    private LocalDate calculateTrueExpDate(@NonNull ShelfLife shelfLife) {

        // Switch on the metric of the shelf life after converting it to a uppercase string
        switch (shelfLife.getMetric().toUpperCase()) {

            case "INDEFINITELY":

                Log.d(LOG_TAG, "Shelf Life Max: Indefinite");

                return LocalDate.MAX;

            case "DAYS":

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Days");

                return printedExpDate.plusDays(shelfLife.getMax());

            case "WEEKS":

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Weeks");

                return printedExpDate.plusWeeks(shelfLife.getMax());

            case "MONTHS":

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Months");

                return printedExpDate.plusMonths(shelfLife.getMax());

            case "YEARS":

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Years");

                return printedExpDate.plusYears(shelfLife.getMax());

            case "PACKAGE USE-BY DATE":

                Log.d(LOG_TAG, "Shelf Life Max: Package use-by date");

                return printedExpDate;

            default:

                throw new RuntimeException("Missing case for shelf life metric: " +
                        shelfLife.getMetric());

        }

    }

}