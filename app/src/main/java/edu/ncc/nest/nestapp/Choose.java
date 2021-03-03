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

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//******************************testing**************************************************************************
import edu.ncc.nest.nestapp.FragmentsCheckExpiration.DisplayTrueExpirationFragment;

public class Choose extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "testing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     *
     * Title : onClick Method -- Whenever a certain button is clicked it would
     * call the method and inside that method would launch an activity and display to the user
     * and then would break afterwords..
     *
     * @param v - The activity that was clicked by the user.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getUPCBtn:
                launchGetUPC();
                break;
            case R.id.guestFormBtn:
                launchGuestForm();
                break;
            case R.id.futureEffortsBtn:
                launchFutureEfforts();
                break;
                // testing **********************
            case R.id.trueDate:
                launchTrueDate();
                break;

        }
    }

    /**
     * launchGuestForm - starts the GuestForm activity
     */
    public void launchGuestForm() {
        Intent intent = new Intent(this, GuestFormTesting.class);
        startActivity(intent);
    }

    /**
     * launchGetUPC - starts the Get UPC activity
     */
    public void launchGetUPC() {
        Intent intent = new Intent(this, CheckExpirationDate.class);
        startActivity(intent);
    }

    /**
     * launchFutureEfforts - starts the Future Efforcts activity
     */
    public void launchFutureEfforts() {
        Intent intent = new Intent(this, FutureEfforts.class);
        startActivity(intent);
    }


    //    ******************************************TESTING
    /**
     * launchTrueDate - starts the DisplayTrueExpirationFragment fragment
     */
    public void launchTrueDate() {
        ((Button)findViewById(R.id.getUPCBtn)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.guestFormBtn)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.futureEffortsBtn)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.trueDate)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.nestTxt)).setVisibility(View.GONE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.RelativeLayoutMain, new DisplayTrueExpirationFragment() ).commit();

    }


}






