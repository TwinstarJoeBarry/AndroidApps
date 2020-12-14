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
import edu.ncc.nest.nestapp.Product;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

public class DisplayTrueExpirationFragment extends Fragment {
    private NestDBDataSource dataSource;
    private int itemId;
    private String upc;
    private int shelfLifeMax;
    private String shelfLifeMatric;
    private Date exp;
    private String TAG = "TESTING";
    private int day;
    private int month;
    private int year;


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


        //  this variable must be assigned to the value from bundle send by previous fragment
        // hardcoded for testing purposes
        // expiration date
        Date exp = new Date(2020, 9, 30);

        // assign item from the bundle received from other fragment
        itemId = 605;

        // assign upc from the bundle received from other fragment
        upc = "123456789";


        try {

            // Instantiating Database
            dataSource = new NestDBDataSource(this.getContext());


            // getting JSON object containing product info from database using item id
            JSONObject product = dataSource.getProductInfoById(itemId);

            // product exist
            if (product.length() != 0) {

//                Log statement to view all the data inside product json object
//                Log.d(TAG, "DisplayTrueExpirationFragment/onCreateView: bundle:" + product.toString());

                // Display item name , upc , category name on fragment_display_true_expiration.xml
                ((TextView) view.findViewById(R.id.item_display)).setText((String) product.get("NAME"));
                ((TextView) view.findViewById(R.id.upc_display)).setText(upc);
                ((TextView) view.findViewById(R.id.category_display)).setText((String) product.get("CATEGORY_NAME"));

                // Getting multiple shelflife records from the product object
                JSONArray shelfLives = (JSONArray) product.get("SHELFLIVES");

                // A product can have multiple shelflife
                // Getting the shortest shelf life array
                JSONObject shelfLife = getShortestShelfLife(shelfLives);


                //Parsing maxdop and metric value from the shortest shelf object
                shelfLifeMax = (int) shelfLife.get("MAX");
                shelfLifeMatric = (String) shelfLife.get("METRIC");

            }

        } catch (JSONException ex) {
            Log.d(TAG, "onViewCreated: " + ex.getMessage());
        } catch (RuntimeException ex) {
            Log.d(TAG, ex.getMessage());
        }


        view.findViewById(R.id.button_display_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Display True Expiration Date
                ((TextView) getView().findViewById(R.id.exp_date_display)).setText(trueExpDate(shelfLifeMax, shelfLifeMatric, exp));
            }
        });


    }


    /**
     * getShortestShelfLife method -
     * This method takes a JSONArray containing shelflives as a parameter and find the shortest shelflife and return its reference as a json object
     *
     * @param shelfLives JSONArray
     * @return shelflife JSONobject
     */
    public JSONObject getShortestShelfLife(JSONArray shelfLives) {


        int index = -1;
        String metric = "";

        try {

            // loop through the Array of JSON objects
            for (int i = 0; i < shelfLives.length(); i++) {

                // get the Json object at index i
                JSONObject shelflife = (JSONObject) shelfLives.get(i);

                //For testing
                //Log.d( TAG, "getShortestShelfLife: " + shelflife.get("METRIC"));

                switch ((String) shelflife.get("METRIC")) {
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
            // json object with shortest shelflife
            return (JSONObject) shelfLives.get(index);

        } catch (JSONException ex) {
            Log.d(TAG, "getShortedShelfLife: " + ex.getMessage());
        }

        return null;
    }


    /**
     * trueExpDate --
     * calculates the True expiration date of the item that has been scanned.
     *
     * @param max     - int
     * @param metric  - String
     * @param expDate - Date
     * @return string containing TrueExpirationDate
     */
    public String trueExpDate(int max, String metric, Date expDate) {


        // convert into lower case
        metric = metric.toLowerCase();

        // separating month, day, and year
        int month = expDate.getMonth();
        int day = expDate.getDate();
        int year = expDate.getYear();

        // variable to store ture expiration date
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