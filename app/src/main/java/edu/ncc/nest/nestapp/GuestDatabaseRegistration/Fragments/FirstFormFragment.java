package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

/**
 * FirstFormFragment: Represents a form that a guest can fill in with their personal information
 * such as, name, phone-number, email-address, ncc-id, postal-address, city, zip-code, birth-date,
 * and date-of-registration. The fragment should then bundle all of the user's inputs and sends them
 * to the next fragment {@link SecondFormFragment}.
 */
public class FirstFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = FirstFormFragment.class.getSimpleName();



    // Variables to store user information
    private EditText name, phone, email, address, city, zip, date, id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_guest_database_registration_first_form,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);



        view.findViewById(R.id.next_button_first_fragment_gRegistration).setOnClickListener(v ->
        {

            // Getting a handle on info from the UI
            name = view.findViewById(R.id.editText);
            phone = view.findViewById(R.id.editText2);
            email = view.findViewById(R.id.editText3);
            id = view.findViewById(R.id.editText4);
            address = view.findViewById(R.id.editText5);
            city = view.findViewById(R.id.editText6);
            zip = view.findViewById(R.id.editText7);
            date = view.findViewById(R.id.editText8);

            String inputName = name.getText().toString();
            String inputPhone = phone.getText().toString();
            String inputEmail = email.getText().toString();
            String inputId = id.getText().toString();
            String inputAddress = address.getText().toString();
            String inputCity = city.getText().toString();
            String inputZip = zip.getText().toString();
            String inputDate = date.getText().toString();

            // For testing purposes
            Log.d(TAG, "The name is: " + inputName);

            Bundle bundle = new Bundle();

            bundle.putString("NAME", inputName);
            bundle.putString("PHONE", inputPhone);
            bundle.putString("EMAIL", inputEmail);
            bundle.putString("ID", inputId);
            bundle.putString("ADDRESS", inputAddress);
            bundle.putString("CITY", inputCity);
            bundle.putString("ZIP", inputZip);
            bundle.putString("DATE", inputDate);

            // Set the bundle as the FragmentResult for SecondFormFragment to retrieve
            getParentFragmentManager().setFragmentResult("firstFormInfo", bundle);

            // Navigate to the SecondFormFragment
            NavHostFragment.findNavController(FirstFormFragment.this)
                    .navigate(R.id.action_DBR_FirstFormFragment_to_SecondFormFragment);

        });

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    /** -- Crashes because of the spinner code************
     Spinner spinner = (Spinner) findViewById(R.id.states_spinner);
     // Create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
     R.array.states_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

}


