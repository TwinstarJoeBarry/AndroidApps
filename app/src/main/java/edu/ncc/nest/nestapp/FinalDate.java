package edu.ncc.nest.nestapp;
/**
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
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

/**
 * <p>Title: NEST - FinalDate Activity</p>
 *
 * <p>Description: This Activity calculates the final expiration date of the item that has been scanned.</p>
 *
 * @author Alireza Farhangi
 */


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @deprecated This Activity is being replaced by a Fragment. ({@link edu.ncc.nest.nestapp.FragmentsCheckExpiration.DisplayTrueExpirationFragment})
 */
@Deprecated
public class FinalDate extends AppCompatActivity {

    /**
     * onCreate --
     * It gets called when the FinalDate Activity launches.
     * @param savedInstanceState - Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //two different expiration dates for testing purposes
        //String expirationDate = "12/10/2019";
        String expirationDate = "11/30/2018";

        // displaying the expiration date
        TextView expirationD = (TextView)findViewById(R.id.expDate);
        expirationD.setText(expirationDate);

        // calling finalExpDate method to calculate the final expiration date
        finalExpDate(expirationDate);


    }


    /**
     * onCreateOptionsMenu --
     * implements the menu options for the toolbar.
     * @param menu - Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * onOptionsItemSelected --
     * handles action bar item clicks.
     * @param item - MenuItem
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * home --
     * goes to the home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }


    /**
     * finalExpDate --
     * calculates the final expiration date of the item that has been scanned.
     * @param expDate - String
     */
    public void finalExpDate(String expDate)
    {
        //String metric = "Months";

        //String maxDop = "4";

        //String metric = "Days";

        //String maxDop = "35";

        // metric dop_pantryLife
        String metric = "Weeks";

        // max dop_pantryLife
        String maxDop = "6";

        metric = metric.toLowerCase();

        // parsing max dop_pantryLife into integer
        int max = Integer.parseInt(maxDop);

        // separating month, day, and year
        int slash = expDate.indexOf("/");

        int secondSlash = expDate.indexOf("/", slash + 1);

        int month = Integer.parseInt(expDate.substring(0, slash));

        int day =  Integer.parseInt(expDate.substring(slash + 1, secondSlash ));

        int year = Integer.parseInt(expDate.substring(secondSlash + 1));

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

        // displaying final expiration date
        TextView finDate = (TextView)findViewById(R.id.finalDate);
       finDate.setText( finalExMonth + "/" + finalExDate + "/" + finalExYear);
    }

}
