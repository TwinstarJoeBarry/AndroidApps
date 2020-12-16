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
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import edu.ncc.nest.nestapp.FragmentsGuestRegistration.GuestFormRegistrationActivity;
import edu.ncc.nest.nestapp.FragmentsGuestRegistration.GuestFormRegistrationStartPageFragment;

public class GuestFormTesting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_form_testing);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.guestRegLocalDbase:
                intent = new Intent(this, GuestFormRegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.guestRegGoogle:
                intent = new Intent(this, GuestForm.class);
                startActivity(intent);
                break;
            case R.id.guestVisitLocalDbase:
                intent = new Intent(this, GuestVisit.class);
                startActivity(intent);
                break;
            case R.id.guesVisitGoogle:
                /*
                intent = new Intent(this, );
                startActivity(intent);
                 */
                break;
        }
    }

    public void addFragment(int fragmentID)
    {
        FragmentManager fm = getSupportFragmentManager();
        GuestFormRegistrationStartPageFragment fragment = new GuestFormRegistrationStartPageFragment();
        fm.beginTransaction().add(fragmentID,fragment).commit();
    }

}