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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VolunteerForm extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;

    EditText firstName, lastName, email, phone, address, city, zip;
    Spinner contact_form;
    CheckBox push_not;
    private Pattern pattern;
    private Matcher matcher;
    Button submit;


    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Retrieves references to fields within user interface
     * also sets up a listener for the submit button
     *
     * @param savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_form);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        phone = findViewById(R.id.phone_number);
        email = findViewById(R.id.email_address);
        city = findViewById(R.id.city);
        zip = findViewById(R.id.zip_code);
        address = findViewById(R.id.postal_address);
        contact_form = findViewById(R.id.contact_spinner);
        push_not = findViewById(R.id.push_not);
        submit = findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
    }

/** onClick - retrieves the view as a parameter, gets the id and checks if
 * the submit button was clicked. Also checks if the checkFill()
 * method is true, if so the intent is started as the new activity
 * @param - v: View
 **/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                if (checkFill() == true) {
                    //intent = new Intent(this, availability.class);
                    startActivity(intent);
                }

                break;

        }
    }


    public boolean checkFill() {
        Toast fix = null;
        if (firstName.getText().toString().equals("")) {
            fix.makeText(this, "Please fill out the First Name field", Toast.LENGTH_SHORT).show();
            return false;

        } else if (lastName.getText().toString().equals("")) {
            fix.makeText(this, "Please fill out the Last Name field", Toast.LENGTH_SHORT).show();
            return false;

        } else if (phone.getText().toString().equals("") || phone.getText().toString().length() != 10 || !isNumeric(phone.getText().toString())) {
            fix.makeText(this, "Please enter a proper phone number with no hyphens i.e. 5165558888", Toast.LENGTH_SHORT).show();
            return false;

        } else if (!isValid(email.getText().toString())) {
            fix.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;

        }
        return true;
    }


    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    //    public boolean properEmail(String address) {
//
//        matcher = pattern.matcher(address);
//        return matcher.matches();
//    }
//
//    public void EmailValidator() {
//        pattern = Pattern.compile(EMAIL_PATTERN);
//    }
    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


}
