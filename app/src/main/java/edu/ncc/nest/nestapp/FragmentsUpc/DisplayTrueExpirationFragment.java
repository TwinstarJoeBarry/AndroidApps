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
import android.icu.text.DateFormat;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
    private String upc;


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
        Date expDate = new Date(2020,9,30);

        itemId = 605;

        upc = "123456789";


        try {

            // Instantiating
            dataSource = new NestDBDataSource(this.getContext());

//            // getting the shelf life from database
//            List<ShelfLife> list = dataSource.getShelfLivesForProduct(itemId);

            // getting product name from database
            JSONObject product = dataSource.getProductInfoById(itemId);

            // product exist
            if (product.length() != 0) {

                Log.d("TESTING", "DisplayTrueExpirationFragment/onCreateView: bundle:" + product.toString());



                ((TextView) view.findViewById(R.id.item_display)).setText((String)product.get("NAME"));
                ((TextView) view.findViewById(R.id.upc_display)).setText(upc);
                ((TextView) view.findViewById(R.id.category_display)).setText((String)product.get("CATEGORY_NAME"));

                    JSONArray shelfLives = (JSONArray) product.get("SHELFLIVES");

                    int shelfLifeIndex = getShortestShelfLife( shelfLives );
                    JSONObject shelfLife = (JSONObject)shelfLives.get(shelfLifeIndex);



                    ((TextView) view.findViewById(R.id.exp_date_display)).setText(trueExpDate(  (int)shelfLife.get("MAX") , (String)shelfLife.get("METRIC") , expDate ));
//                ((TextView)view.findViewById(R.id.category_display)).setText(product.getString("CATEGORY_NAME"));
//                product.get("exp_date_display").toString()

            }


        }
        catch(JSONException ex){
            Log.d("TESTING", "onViewCreated: " + ex.getMessage());
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

    public int getShortestShelfLife(JSONArray shelfLives){

        int index = -1;
        String metric = "";
        for(int i = 0; i < shelfLives.length(); i++){

            try {
                JSONObject shelflife = (JSONObject) shelfLives.get(i);
                Log.d("TESTING", "getShortestShelfLife: " + shelflife.get("METRIC"));
                switch ((String) shelflife.get("METRIC")){
                    case "Years":
                           if( metric == ""){
                              metric = "Years";
                              index = i;
                           }
                        break;
                    case "Months":
                        if( metric == "" || metric == "Years"){
                            metric = "Months";
                            index = i;
                        }
                        break;
                    case "Weeks":
                        if( metric == "" || metric == "Years"|| metric == "Months"){
                            metric = "Weeks";
                            index = i;
                        }
                        break;
                    case "Days":
                        if( metric == "" || metric == "Years" || metric == "Months"|| metric == "Weeks"){
                            metric = "Days";
                            index = i;
                        }
                        break;
                    default:
                        Log.d("TESTING", "getShortedShelfLife: Error invalid option");
                }


            }
            catch (JSONException ex){
                Log.d("TESTING", "getShortedShelfLife: " + ex.getMessage());
            }
        }

        Log.d("TESTING", "getShortedShelfLife: index" + index);
        return index;
    }


    /**
     * trueExpDate --
     * calculates the true expiration date of the item.
     */
    public String trueExpDate1(int max, String metric, Date expDate)
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

            finalExDate = expDate.getDay();
            finalExYear = expDate.getYear();
            finalExMonth = expDate.getMonth() + max;

            while(finalExMonth > 12)
            {
                finalExMonth = finalExMonth - 12;
                finalExYear = expDate.getYear() + 1;
                expDate.setYear( finalExYear );
            }

        }

        // if metric is days
        if(metric.equals("days"))
        {
            finalExYear = expDate.getYear();
            finalExMonth = expDate.getMonth();

            finalExDate = expDate.getDay() + max;

            // months that have 31 days
            if(expDate.getMonth() == 1 || expDate.getMonth() == 3 || expDate.getMonth() == 5 || expDate.getMonth() == 7 || expDate.getMonth() == 8 || expDate.getMonth() == 10 || expDate.getMonth() == 12) {
                if(finalExDate > 31)
                {
                    finalExDate = finalExDate - 31;
                    finalExMonth = finalExMonth + 1;
                    if(finalExMonth > 12)
                    {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = expDate.getYear() + 1;

                    }
                }

            }

            // months that have 30 days
            if(expDate.getMonth() == 2 || expDate.getMonth() == 4 || expDate.getMonth() == 6 || expDate.getMonth() == 9 || expDate.getMonth() == 11)
            {
                if(finalExDate > 30)
                {
                    finalExDate = finalExDate - 30;
                    finalExMonth = finalExMonth + 1;
                    if(finalExMonth > 12)
                    {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = expDate.getYear() + 1;

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
            finalExMonth = expDate.getMonth();
            finalExDate = expDate.getDay();
            finalExYear = expDate.getYear() + max;
        }

//        logging the final date
//        Log.d("TESTING", "finalExpDate1: " + finalExMonth + "/" + finalExDate + "/" + finalExYear);

        return finalExMonth + "/" + finalExDate + "/" + finalExYear;
    }



    /**
     * finalExpDate --
     * calculates the final expiration date of the item that has been scanned.
     * @param expDate - String
     */
    public String trueExpDate(int max, String metric, Date expDate)
    {


        metric = metric.toLowerCase();

        // separating month, day, and year
        int month = expDate.getMonth();

        int day =  expDate.getDate();

        int year = expDate.getYear();

        Log.d("TESTING", "trueExpDate: max: " + max + " Metric: " + metric );
        Log.d("TESTING", "trueExpDate: month: " + month + " day: " + day + " year:" + year );

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

//        // displaying final expiration date
//        TextView finDate = (TextView)findViewById(R.id.finalDate);
//        finDate.setText( finalExMonth + "/" + finalExDate + "/" + finalExYear);

        return finalExMonth + "/" + finalExDate + "/" + finalExYear;
    }

}
