package edu.ncc.nest.nestapp;
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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments.StartFragment;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.Activities.GuestDatabaseRegistrationActivity;
import edu.ncc.nest.nestapp.GuestGoogleSheetRegistration.Activities.GuestGoogleSheetRegistrationActivity;
import edu.ncc.nest.nestapp.GuestVisit.Activities.GuestVisitActivity;

public class GuestFormTesting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_form_testing);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //implements the menu options for the toolbar
    //this method display the Home button in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //instead of inflating menu_main, now this class inflates choose_menu_main.xml, this way
        // allowing the future efforts button to only appear at the app bar of the launch UI
        inflater.inflate(R.menu.menu_main, menu);

        return true;

    }

    /**
     * onOptionsSelected method --
     * description: this method makes you comeback to the launch UI when the home button is clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_btn) {
            home();
            return true;
        }
        if(item.getItemId() == R.id.login_btn){
            launchLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method --
     * description: this method goes to the nest home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    /**
     * launchLoginActivity - starts the Login Activity
     */
    public void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.guestRegLocalDbase:
                intent = new Intent(this, GuestDatabaseRegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.guestVisitLocalDbase:
                intent = new Intent(this, GuestVisitActivity.class);
                startActivity(intent);
                break;
            case R.id.guesVisitGoogle:
                /*
                intent = new Intent(this, );
                startActivity(intent);
                 */
                break;
            case R.id.home_btn:
                //need help
                 break;
        }
    }

    public void addFragment(int fragmentID)
    {
        FragmentManager fm = getSupportFragmentManager();
        StartFragment fragment = new StartFragment();
        fm.beginTransaction().add(fragmentID,fragment).commit();
    }

}