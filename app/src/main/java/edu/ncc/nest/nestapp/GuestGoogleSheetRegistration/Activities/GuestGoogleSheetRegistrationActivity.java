package edu.ncc.nest.nestapp.GuestGoogleSheetRegistration.Activities;
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
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.R;

/**
 * GuestGoogleSheetRegistrationActivity - Underlying Activity for fragments related to registering a
 * guest with google sheets
 */
public class GuestGoogleSheetRegistrationActivity extends AppCompatActivity {

    public static final String TAG = GuestGoogleSheetRegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "In GuestGoogleSheetRegistrationActivity onCreate()");

        setContentView(R.layout.activity_guest_google_sheet_registration);

        setSupportActionBar(findViewById(R.id.google_sheet_registration_toolbar));

    }

    /**
     * home method - goes to the home screen
     */
    public void home() {

        Intent intent = new Intent(this, Choose.class);

        startActivity(intent);

    }

}
