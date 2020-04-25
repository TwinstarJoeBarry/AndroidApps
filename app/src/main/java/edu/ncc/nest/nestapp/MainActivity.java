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
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private static final String TAG = "testing";

    /**
     * onCreate--
     * MainActivity is initialized here, calls SetBeackgroundDrawableResource to set
     * the background image veg_table
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getWindow().setBackgroundDrawableResource(R.drawable.veg_table);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onCLick method - responds to a click by the user
     * @paran the view object clicked by the user
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgotPass:
//                intent = new Intent(this, .class);
//                startActivity(intent);
                break;
            case R.id.signUp:
                intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.log_in_button:
                intent = new Intent(this, Choose.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * home method - goes to the home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }


}
