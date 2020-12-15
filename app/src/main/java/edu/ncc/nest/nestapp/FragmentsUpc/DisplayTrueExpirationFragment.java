package edu.ncc.nest.nestapp.FragmentsUpc;
/**
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.Product;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

public class DisplayTrueExpirationFragment extends Fragment {
    private NestDBDataSource dataSource;
    private String TAG = "TESTING";
    private NestUPC product = null;
    private String exp;
    private ShelfLife shelfLife;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_true_expiration, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Receiving the bundle
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle data) {

                // Parsing the product from the bundle
                product = (NestUPC) data.getParcelable("foodItem");
                // Parsing expiration date from the bundle
                exp = data.getString("DATE");
            }

        });


        //TESTING ********************* comment both lines after testing,
        product = new NestUPC("123456789","Tuscan","", 644, "Milk","",2,"Dairy");
        exp = "06/10/20";



        // product exists
        if (product != null) {

            // Display item name , upc , category name on fragment_display_true_expiration.xml
            ((TextView) view.findViewById(R.id.item_display)).setText(product.getProductName());
            ((TextView) view.findViewById(R.id.upc_display)).setText(product.getUpc());
            ((TextView) view.findViewById(R.id.category_display)).setText(product.getCatDesc());

            // Instantiating Database
            dataSource = new NestDBDataSource(this.getContext());

            // getting the product shelf life from the database
            List<ShelfLife> shelfLives = dataSource.getShelfLivesForProduct(product.getProductId());

            Log.d(TAG, "DisplayTrueExpirationFragment: onViewCreated: " + shelfLives.toString());    //********************   Testing

            // get the shortest shelf life from the list of shelf lives
            shelfLife = getShortestShelfLife(shelfLives);
        }
        // product doesn't exist
        else {
            Log.d(TAG, "DisplayTrueExpirationFragment: onViewCreated: product doesn't exist");
        }


        view.findViewById(R.id.button_display_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // product exist
                if (product != null)
                    // Display True Expiration Date
                    ((TextView) getView().findViewById(R.id.exp_date_display)).setText(trueExpDate(shelfLife, exp));
            }
        });


    }


    /**
     * getShortestShelfLife method -
     * This method takes a List of shelf lives as a parameter and find the shortest shelflife which then gets returned
     *
     * @param shelfLives List<ShelfLife></ShelfLife>
     * @return shelflife ShelfLife
     */
    public ShelfLife getShortestShelfLife(List<ShelfLife> shelfLives) {


        int index = -1;
        String metric = "";

        ShelfLife shelfLife;


        // loop through the list of shelf lives
        for (int i = 0; i < shelfLives.size(); i++) {

            shelfLife = shelfLives.get(i);

            switch (shelfLife.getMetric()) {
                case "Years":
                    if (metric == "") {
                        metric = "Years";
                        index = i;
                    }
                    break;
                case "Months":
                    if (metric == "" || metric == "Years") {
                        metric = "Months";
                        index = i;
                    }
                    break;
                case "Weeks":
                    if (metric == "" || metric == "Years" || metric == "Months") {
                        metric = "Weeks";
                        index = i;
                    }
                    break;
                case "Days":
                    if (metric == "" || metric == "Years" || metric == "Months" || metric == "Weeks") {
                        metric = "Days";
                        index = i;
                    }
                    break;
                default:
                    Log.d(TAG, "getShortedShelfLife: Error invalid option");
                    break;
            }

        }

        return shelfLives.get(index);
    }


    /**
     * trueExpDate --
     * calculates the true expiration date of the item that has been scanned.
     * @param shelfLife - ShelfLife
     * @param expDate - String
     */
    public String trueExpDate(ShelfLife shelfLife, String expDate) {

        // metric dop_pantryLife
        String metric = shelfLife.getMetric();

        metric = metric.toLowerCase();

        // max dop_pantryLife
        int max = shelfLife.getMax();

        // separating month, day, and year
        int slash = expDate.indexOf("/");

        int secondSlash = expDate.indexOf("/", slash + 1);

        int month = Integer.parseInt(expDate.substring(0, slash));

        int day = Integer.parseInt(expDate.substring(slash + 1, secondSlash));

        int year = Integer.parseInt(expDate.substring(secondSlash + 1));

        int finalExMonth = 0;
        int finalExDate = 0;
        int finalExYear = 0;

        // if metric is weeks
        if (metric.equals("weeks")) {
            metric = "days";
            max = 7 * max;
        }

        // if metric is months
        if (metric.equals("months")) {

            finalExDate = day;
            finalExYear = year;
            finalExMonth = month + max;

            while (finalExMonth > 12) {
                finalExMonth = finalExMonth - 12;
                finalExYear = year + 1;
                year = finalExYear;
            }

        }

        // if metric is days
        if (metric.equals("days")) {
            finalExYear = year;
            finalExMonth = month;

            finalExDate = day + max;

            // months that have 31 days
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (finalExDate > 31) {
                    finalExDate = finalExDate - 31;
                    finalExMonth = finalExMonth + 1;
                    if (finalExMonth > 12) {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }
                }

            }

            // months that have 30 days
            if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
                if (finalExDate > 30) {
                    finalExDate = finalExDate - 30;
                    finalExMonth = finalExMonth + 1;
                    if (finalExMonth > 12) {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }

                }

            }

            // if the final date is greater than 31
            while (finalExDate > 31) {
                finalExDate = finalExDate - 31;
                finalExMonth = finalExMonth + 1;

                // if the final month is greater than 12
                if (finalExMonth > 12) {
                    finalExMonth = finalExMonth - 12;
                    finalExYear = finalExYear + 1;

                }
            }

        }

        // if metric is years
        if (metric.equals("years")) {
            finalExMonth = month;
            finalExDate = day;
            finalExYear = year + max;
        }


        return finalExMonth + "/" + finalExDate + "/" + finalExYear;
    }


}