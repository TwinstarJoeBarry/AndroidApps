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
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.ncc.nest.nestapp.FinalDate;
import edu.ncc.nest.nestapp.InventoryEntry;
import edu.ncc.nest.nestapp.InventoryInfoSource;
import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.Product;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

public class DisplayTrueExpirationFragment extends Fragment {
    private NestDBDataSource dataSource;
    private int itemId;
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
        day = 20;
        month = 7;
        year = 2020;
        itemId = 120;


        try {

            // Instantiating
            dataSource = new NestDBDataSource(this.getContext());

//            // getting the shelf life from database
//            List<ShelfLife> list = dataSource.getShelfLivesForProduct(itemId);

            // getting product name from database
            Bundle product = dataSource.getProductInfoById(itemId);

            // product exist
            if (product != null) {

                Log.d("TESTING", "DisplayTrueExpirationFragment/onCreateView: bundle:" + product.toString());
               
                ((TextView)view.findViewById(R.id.item_display)).setText(product.getString("NAME"));

                ((TextView)view.findViewById(R.id.category_display)).setText(product.getString("CATEGORY_NAME"));
//                try {
//
//                    JSONArray shelfLives = new JSONArray(product.getString("SHELFLIVES"));
//                    JSONObject shelfLifeRecord = shelfLives.getJSONObject(0);
////                    trueExpDate(shelfLifeRecord.getInt("MAX"),shelfLifeRecord.getString("METRIC"),month , day,year);
//
//                }catch(JSONException ex){
//                    Log.d("TESTING", "onViewCreated: " + ex.getMessage());
//                }


            }


        }
        catch(RuntimeException ex){
            Log.d("TESTING", ex.getMessage());
        }



        view.findViewById(R.id.button_display_date_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigation has yet to be set up in the nav_graph.xml
                NavHostFragment.findNavController(DisplayTrueExpirationFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
            }
        });


    }


    /**
     * trueExpDate --
     * calculates the true expiration date of the item.
     * @param month - int
     * @param day - int
     * @param year -iny
     */
    public void trueExpDate(int max, String metric, int month, int day, int year)
    {
        metric = metric.toLowerCase();


        int finalExMonth = 0;
        int finalExDate = 0;
        int finalExYear = 0;

        // if metric is weeks
        if(metric.equals("weeks"))
        {
            metric = "days";
            max = 7*max;
        }

        // if metric is months
        if(metric.equals("months"))
        {

            finalExDate = day;
            finalExYear = year;
            finalExMonth = month + max;

            while(finalExMonth > 12)
            {
                finalExMonth = finalExMonth - 12;
                finalExYear = year + 1;
                year = finalExYear;
            }

        }

        // if metric is days
        if(metric.equals("days"))
        {
            finalExYear = year;
            finalExMonth = month;

            finalExDate = day + max;

            // months that have 31 days
            if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if(finalExDate > 31)
                {
                    finalExDate = finalExDate - 31;
                    finalExMonth = finalExMonth + 1;
                    if(finalExMonth > 12)
                    {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }
                }

            }

            // months that have 30 days
            if(month == 2 || month == 4 || month == 6 || month == 9 || month == 11)
            {
                if(finalExDate > 30)
                {
                    finalExDate = finalExDate - 30;
                    finalExMonth = finalExMonth + 1;
                    if(finalExMonth > 12)
                    {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }

                }

            }

            // if the final date is greater than 31
            while(finalExDate > 31)
            {
                finalExDate = finalExDate - 31;
                finalExMonth = finalExMonth + 1;

                // if the final month is greater than 12
                if(finalExMonth > 12)
                {
                    finalExMonth = finalExMonth - 12;
                    finalExYear = finalExYear + 1;

                }
            }

        }

        // if metric is years
        if(metric.equals("years"))
        {
            finalExMonth = month;
            finalExDate = day;
            finalExYear = year + max;
        }

//        logging the final date
        Log.d("TESTING", "finalExpDate1: " + finalExMonth + "/" + finalExDate + "/" + finalExYear);
    }
}
