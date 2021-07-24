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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.sql.Time;
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
 * Displays the status of the selected item's expiration using a red, yellow, or green icon that
 * represents whether or not the item has truly expired.
 *
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

            ((TextView) view.findViewById(R.id.storage_tips)).setText(
                    dop_pantryLife.getTips() != null ? dop_pantryLife.getTips() : "N/A");

            trueExpDate = calculateTrueExpDate(foodItem, dop_pantryLife);

            if (trueExpDate != null) {

                ((TextView) view.findViewById(R.id.true_exp_date)).setText(formatDate(trueExpDate));

                if (trueExpDate.getTime() != 0L) {

                    long numDays = daysUntilExpiration(trueExpDate);

                    if (numDays <= 0) {

                        statusMsg.setText("The item has expired and should be discarded");

                        statusIcon.setImageResource(R.drawable.ic_delete);

                    } else if (numDays < 30) {

                        statusMsg.setText("The item is within 30 days of expiration");

                        statusIcon.setImageResource(R.drawable.ic_warning);

                    } else {

                        statusMsg.setText("The item does not expire for 30 days or more");

                        statusIcon.setImageResource(R.drawable.ic_check_mark);

                    }

                } else {

                    statusMsg.setText("The items expiration date is indefinite");

                    statusIcon.setImageResource(R.drawable.ic_indefinite);

                }

            } else {

                ((TextView) view.findViewById(R.id.true_exp_date)).setText("Unknown");

                statusMsg.setText("The shelf life of this item is unknown");

                statusIcon.setImageResource(R.drawable.ic_help);
                
            }

            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        view.findViewById(R.id.more_info_btn).setOnClickListener(more_info_btn -> {

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            result.putSerializable("printedExpDate", printedExpDate);

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            // Navigate to the proper fragment
            NavHostFragment.findNavController(StatusFragment.this)
                    .navigate(R.id.action_CED_StatusFragment_to_DisplayTrueExpirationFragment);

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Calculates the true expiration date range of an item based on it's shelf life and printed
     * expiration date.
     * @param foodItem The selected item.
     * @param shelfLife The index of the shelf life type, see ShelfLife.
     * @return The true expiration date of the given item.
     */
    private Date calculateTrueExpDate(NestUPC foodItem, ShelfLife shelfLife) {

        if (shelfLife == null || shelfLife.getMetric() == null)

            return null;

        // Get an instance of the Calendar class
        Calendar max = Calendar.getInstance();

        // Update the time to the selected item's printed expiration date
        max.setTime(printedExpDate);

        switch (shelfLife.getMetric().toLowerCase()) {

            case "indefinitely":

                return new Date(0L);

            case "days":

                // Add max number of days to the printed expiration date
                max.add(Calendar.DAY_OF_MONTH, shelfLife.getMax());

                return max.getTime();

            case "weeks":

                // Add max number of weeks to the printed expiration date
                max.add(Calendar.WEEK_OF_MONTH, shelfLife.getMax());

                return max.getTime();

            case "months":

                // Add max number of months to the printed expiration date
                max.add(Calendar.MONTH, shelfLife.getMax() - 1);

                return max.getTime();

            case "years":

                // Add max number of years to the printed expiration date
                max.add(Calendar.YEAR, shelfLife.getMax());

                max.add(Calendar.MONTH, -1);

                return max.getTime();

            default:

                return null;

        }

    }

    /**
     *
     * @param expDate
     * @return
     */
    private long daysUntilExpiration(Date expDate) {

        return TimeUnit.MILLISECONDS.toDays(
                expDate.getTime() - System.currentTimeMillis());

    }

    /**
     *
     * @param date
     * @return
     */
    private String formatDate(Date date) {

        if (date != null) {

            // Format the date to the pattern of MM/dd/yyyy and return the result
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

            return sdf.format(date.getTime());

        } else

            return "Unknown";

    }

}