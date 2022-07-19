package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Activities;

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
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

/**
 * GuestDatabaseRegistrationActivity: This is the underlying activity for the fragments of the
 * GuestDatabaseRegistration feature.
 */
public class GuestDatabaseRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Clear the back data for every new activity cycle of guest registration
        deleteSharedPreferences("BackFrag2");

        setContentView(R.layout.activity_guest_database_registration);

        setSupportActionBar(findViewById(R.id.database_registration_toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home_btn)

            home();

        return super.onOptionsItemSelected(item);

    }

    /**
     * home --
     * Starts the {@link Choose} Activity
     */
    public void home() {

        Intent intent = new Intent(this, Choose.class);

        startActivity(intent);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) { super.onSaveInstanceState(outState); }

    @Override
    public void onRestoreInstanceState(Bundle inState)
    {
        super.onSaveInstanceState(inState);
    }
}
