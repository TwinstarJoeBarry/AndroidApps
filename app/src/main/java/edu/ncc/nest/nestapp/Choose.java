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

import android.content.DialogInterface;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            case R.id.addToInventoryBtn:
                launchAddToInventory();
                break;
            case R.id.inventoryBtn:
                launchInventory();
                break;
            case R.id.scheduleBtn:
                launchSchedule();
                break;
            case R.id.guestFormBtn:
                launchGuestForm();
                break;
            case R.id.volunteerFormBtn:
                launchVolForm();
                break;
            case R.id.signUpBtn:
                createAccountDialog();
                break;
            case R.id.finalDBtn:
                launchFinalDate();
                break;
            case R.id.donateBtn:
                launchDonate();
                break;
            case R.id.interfaceTestBtn:
                launchInterfaceTest();
                break;
            case R.id.getUPCBtn:
                launchGetUPC();
                break;
        }
    }

    /**
     * createAccountDialog method - this method creates an alert dialog when the 'Create Nest Account' button is clicked. An alert dialog
     * will be displayed telling the user what creating an account entails, and will ask them if they want to create an account or not.
     * If they select 'Yes' they will be directed to the SignUp activity. If they select 'No' the dialog will close.
     */
    public void createAccountDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Choose.this);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage("Creating a NEST account will allow you to receive notifications relating to guest, donator, or volunteer " +
                "opportunities. You will be allowed to manage your notification preferences once your account is created. Your name, email " +
                "address, and phone number will be required." + "\n" + "Create an account?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //launch signUp activity
                launchSignUp();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertBuilder.create();

        alert.show();
    }

    /**
     * launchDonate - starts the Donate activity
     */
    public void launchDonate() {
        Intent intent = new Intent(this, Donate.class);
        startActivity(intent);
    }

    /**
     * launchInventory - starts the Inventory activity
     */
    public void launchInventory() {
        Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
    }

    /**
     * launchSchedule - starts the Schedule activity
     */
    public void launchSchedule() {
        Intent intent = new Intent(this, Schedule.class);
        startActivity(intent);
    }

    /**
     * launchGuestForm - starts the GuestForm activity
     */
    public void launchGuestForm() {
        Intent intent = new Intent(this, GuestForm.class);
        startActivity(intent);
    }

    /**
     * launchSignUp - starts the SignUp activity
     */
    public void launchSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    /**
     * launchVolForm - starts the Volunteer activity
     */
    public void launchVolForm() {
        Intent intent = new Intent(this, VolunteerForm.class);
        startActivity(intent);
    }

    /**
     * launchScanner - starts the Scanner activity
     */
    public void launchScanner() {
        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }

    public void launchFinalDate()
    {
        Intent intent = new Intent(this, FinalDate.class);
        startActivity(intent);
    }

    /**
     * launchInterfaceTest - starts the UI test activity
     */
    public void launchInterfaceTest() {
        Intent intent = new Intent(this, InterfaceTests.class);
        startActivity(intent);
    }

    /**
     * launchVolForm - starts the Volunteer activity
     */
    public void launchAddToInventory()
    {
        Intent intent = new Intent(this, addToInventory.class);
        startActivity(intent);
    }

    /**
     * launchEnterUPC - starts the Enter UPC activity
     */
    public void launchEnterUPC() {
        Intent intent = new Intent(this, EnterUPC.class);
        startActivity(intent);
    }

    /**
     * launchGetUPC - starts the Get UPC activity
     */
    public void launchGetUPC() {
        Intent intent = new Intent(this, GetUPC.class);
        startActivity(intent);
    }
}


