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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    private NestDBDataSource dataSource;

    private Date printedExpDate;

    private Date trueExpDate;

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
            printedExpDate = (Date) result.getSerializable("printedExpDate");

            assert foodItem != null && printedExpDate != null : "Failed to retrieve required data";

            // Get the product's dop_pantryLife shelf life from the database
            ShelfLife dop_pantryLife = dataSource.getItemShelfLife(
                    foodItem.getProductId(), ShelfLife.DOP_PL);

            updateShelfLifeTips(view, dop_pantryLife);

            trueExpDate = calculateTrueExpDate(dop_pantryLife);

            Log.d(LOG_TAG, "Printed Expiration Date: " + formatDate(printedExpDate));

            if (trueExpDate == null) {

                Log.w(LOG_TAG, "True Expiration Date: Unknown");

                ((TextView) view.findViewById(R.id.true_exp_date)).setText(R.string.unknown);

                statusMsg.setText(R.string.status_fragment_unknown_msg);

                statusIcon.setImageResource(R.drawable.ic_help);

            } else if (trueExpDate.getTime() != 0L) {

                Log.d(LOG_TAG, "True Expiration Date: " + formatDate(trueExpDate));

                ((TextView) view.findViewById(R.id.true_exp_date)).setText(formatDate(trueExpDate));

                long numDays = daysUntilDate(trueExpDate);

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

            } else {

                Log.d(LOG_TAG, "True Expiration Date: Indefinite");

                ((TextView) view.findViewById(R.id.true_exp_date)).setText(R.string.indefinite);

                statusMsg.setText(R.string.status_fragment_indefinite_msg);

                statusIcon.setImageResource(R.drawable.ic_indefinite);

            }

            // Make sure to clear the listener so we can use the same request key in other fragments
            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        view.findViewById(R.id.more_info_btn).setOnClickListener(more_info_btn -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate);

            result.putSerializable("trueExpDate", trueExpDate);

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
     * Calculates the true expiration date of an item based on it's shelf life and printed
     * expiration date. ({@code printedExpDate} + {@link ShelfLife#getMax()} - 1 Month).
     * @param shelfLife The {@link ShelfLife} to use when calculate the date.
     * @return The true expiration {@link Date} of the given item.
     */
    private Date calculateTrueExpDate(ShelfLife shelfLife) {

        if (shelfLife == null || shelfLife.getMetric() == null)

            return null;

        // Get an instance of the Calendar class
        Calendar max = Calendar.getInstance();

        // Update the time to the selected item's printed expiration date
        max.setTime(printedExpDate);

        // Switch on the metric of the shelf life after converting it to a lowercase string
        switch (shelfLife.getMetric().toLowerCase()) {

            case "indefinitely":

                Log.d(LOG_TAG, "Shelf Life Max: Indefinite");

                // Return a date with the time set to zero to represent indefinite
                return new Date(0L);

            case "days":

                // Add max number of days to the printed expiration date
                max.add(Calendar.DAY_OF_MONTH, shelfLife.getMax());

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Days");

                return max.getTime();

            case "weeks":

                // Add max number of weeks to the printed expiration date
                max.add(Calendar.WEEK_OF_MONTH, shelfLife.getMax());

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Weeks");

                return max.getTime();

            case "months":

                // Add max number of months to the printed expiration date
                max.add(Calendar.MONTH, shelfLife.getMax());

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Months");

                return max.getTime();

            case "years":

                // Add max number of years to the printed expiration date
                max.add(Calendar.YEAR, shelfLife.getMax());

                Log.d(LOG_TAG, "Shelf Life Max: " + shelfLife.getMax() + " Years");

                return max.getTime();

        }

        return null;

    }

    /**
     * Calculates and returns number of day from the current system date until the given date.
     * @param date The {@link Date} to use when performing the calculation.
     * @return The number of days until the given date.
     */
    private long daysUntilDate(@NonNull Date date) {

        // Convert dates to milliseconds, perform subtraction, then convert millis to days
        return TimeUnit.MILLISECONDS.toDays(date.getTime() - System.currentTimeMillis());

    }

    /**
     * Formats a date object's time as a date {@link String} in the format of "MM/dd/yyy"
     * @param date The date to format
     * @return The formatted date string.
     */
    private String formatDate(@NonNull Date date) {

        // Format the date to the pattern of MM/dd/yyyy and return the result
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        return sdf.format(date.getTime());

    }

    /**
     * Updates the storage tips {@link TextView} object in this fragment's layout to display any
     * available storage tips.
     * @param shelfLife The shelf life to get the tips from.
     */
    private void updateShelfLifeTips(@NonNull View view, ShelfLife shelfLife) {

        if (shelfLife != null && shelfLife.getTips() != null)

            ((TextView) view.findViewById(R.id.storage_tips)).setText(shelfLife.getTips());

        else

            ((TextView) view.findViewById(R.id.storage_tips)).setText("N/A");

    }

}