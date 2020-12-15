package edu.ncc.nest.nestapp.FragmentsGuestRegistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.GuestFormHelper;
import edu.ncc.nest.nestapp.R;
import  androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragmentGuestRegistration  extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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



        //database where we will store user information
        GuestFormHelper db;

        //variables to store user information
        EditText name, email, phone, date, address, city, zip,id;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_guest_form);

            //Toolbar toolbar = findViewById(R.id.toolbar);
            // setSupportActionBar(toolbar);
            return inflater.inflate(R.layout.fragment_guest_form_registration_start_page, container, false);
        }
            //creating the database and passing the correct context as the argument
           // db = new GuestFormHelper(this);

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          //  view.findViewById(R.id.fragment_gue)
//getting a handle on info from the UI
        name = (EditText) getView().findViewById(R.id.editText);
        phone = (EditText)getView(). findViewById(R.id.editText2);
        email = (EditText) getView().findViewById(R.id.editText3);
        address = (EditText)getView().findViewById(R.id.editText5);
        city = (EditText)getView().findViewById(R.id.editText6);
        zip = (EditText)getView().findViewById(R.id.editText7);
        date = (EditText)getView(). findViewById(R.id.editText8);
        id= (EditText)getView().findViewById(R.id.editText4);

    }

        //implements the menu options for the toolbar
      //  @Override
        public boolean onCreateOptionsMenu(Menu menu) {

          //  MenuInflater inflater = getMenuInflater();
            //inflater.inflate(R.menu.menu_main, menu);
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
         * Submits user-data when click is received.
         * Notifies user in a toast if the adding is successful
         *
         * @param view
         */


        public void onClick(View view) {

            //variable used for checks
            boolean ins = false;

            //adding the values into the database if submit button is pressed
            if (view.getId() == R.id.done_button) {

                // NOTE: The parameter 'barcode' was recently added to this method
                // TODO: Update parameter 'barcode' to the barcode representing this user
                ins = db.insertData(name.getText().toString(), email.getText().toString(), phone.getText().toString(), date.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), null);

            }

            //notifying the user if the add was successful
            if (ins) {
              //  Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_LONG).show();
            }

        }
/** -- Crashes because of the spinner code************
 Spinner spinner = (Spinner) findViewById(R.id.states_spinner);
 // Create an ArrayAdapter using the string array and a default spinner layout
 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
 R.array.states_array, android.R.layout.simple_spinner_item);
 */
// Specify the layout to use when the list of choices appears
/*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
spinner.setAdapter(adapter);

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    ...

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    Spinner spinner = (Spinner) findViewById(R.id.spinner);
    spinner.setOnItemSelectedListener(this);*/


        /**
         * home method - goes to the home screen
         */
        public void home() {
           // Intent intent = new Intent(this, Choose.class);
            //startActivity(intent);
        }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


