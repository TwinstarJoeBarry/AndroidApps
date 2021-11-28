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

import android.graphics.Color;
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

// imports binding for the used layout
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFirstFormBinding;


/**
 * FirstFormFragment: Represents a form that a guest can fill in with their personal information
 * such as, name, phone-number, email-address, ncc-id, postal-address, city, zip-code, birth-date,
 * and date-of-registration. The fragment should then bundle all of the user's inputs and sends them
 * to the next fragment {@link SecondFormFragment}.
 */
public class FirstFormFragment extends Fragment {

    public static final String TAG = FirstFormFragment.class.getSimpleName();

    // declare the binding for the current fragment.
    private FragmentGuestDatabaseRegistrationFirstFormBinding binding;

    // Database where we will store user information
    private GuestRegistrySource db;

    // Variables to store user information
    private EditText firstName, lastName, phoneNumber, nccID;

    private String inputFirstName, inputLastName, inputPhoneNumber, inputNCCID;
    private Bundle result = new Bundle();
    private boolean validInput = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentGuestDatabaseRegistrationFirstFormBinding.inflate(inflater, container, false);
        return binding.getRoot();

        /* for binding, change this commented code with the code currently being used
        return inflater.inflate(R.layout.fragment_guest_database_registration_first_form,
                container, false);
        */
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        binding.nextButtonFirstFragmentGRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                inputFirstName = ((EditText) getView().findViewById(R.id.guestreg_fname)).getText().toString();
                inputLastName = ((EditText) getView().findViewById(R.id.guestreg_lname)).getText().toString();
                inputPhoneNumber = ((EditText) getView().findViewById(R.id.guestreg_phone)).getText().toString();
                inputNCCID = ((EditText) getView().findViewById(R.id.guestreg_nccid)).getText().toString();

                Log.d(TAG, "first name: " + inputFirstName);
                Log.d(TAG, "last name: " + inputLastName);
                Log.d(TAG, "phone number: " + inputPhoneNumber);
                Log.d(TAG, "NCC ID: " + inputNCCID);

                if(inputFirstName.length() == 0){
                    binding.enterFirstName.setTextColor(Color.RED);
                    binding.enterFirstName.setText("You must enter a first name.");
                    validInput = false;
                }
                else{
                    binding.enterFirstName.setTextColor(Color.WHITE);
                    binding.enterFirstName.setText("Enter your first name.");
                    result.putString("First Name", inputFirstName);
                    validInput = true;
                }

                if(inputLastName.length() == 0){
                    binding.enterLastName.setTextColor(Color.RED);
                    binding.enterLastName.setText("You must enter a last name.");
                    validInput = false;
                }
                else{
                    binding.enterLastName.setTextColor(Color.WHITE);
                    binding.enterLastName.setText("Enter your last name.");
                    result.putString("Last Name", inputLastName);
                    validInput = true;
                }

                if(inputPhoneNumber.length() != 10){
                    binding.enterPhoneNumber.setTextColor(Color.RED);
                    binding.enterPhoneNumber.setText("You must enter a valid phone number.");
                    validInput = false;
                }
                else{
                    binding.enterPhoneNumber.setTextColor(Color.WHITE);
                    binding.enterPhoneNumber.setText("Enter your phone number.");
                    result.putString("Phone Number", inputPhoneNumber);
                    validInput = true;
                }

                if(inputNCCID.length() != 8){
                    binding.enterYourNccId.setTextColor(Color.RED);
                    binding.enterYourNccId.setText("You must enter a valid NCC ID,");
                    validInput = false;
                }
                else{
                    binding.enterYourNccId.setTextColor(Color.WHITE);
                    binding.enterYourNccId.setText("Enter your NCC ID.");
                    result.putString("NCC ID", inputNCCID);
                    validInput = true;
                }

                if(validInput){
                    getParentFragmentManager().setFragmentResult("sending_first_form_fragment_info", result);

                    /*
                    // To test the doesExist() method uncomment this,
                    // and comment out the findNavController() that's currently being used

                    // Creating the database and passing the correct context as the argument
                    db = new GuestRegistrySource(requireContext());

                    // if true, user already exist in the db, else user can register
                    if (db.doesExist(inputPhoneNumber, inputNCCID)) {
                        Log.d(TAG, "onClick: An user already registered with this phone number or NCC ID!");

                        binding.enterYourNccId.setTextColor(Color.RED);
                        binding.enterYourNccId.setText("This NCC ID or phone number already being used!");

                        binding.enterPhoneNumber.setTextColor(Color.RED);
                        binding.enterPhoneNumber.setText("This NCC ID or phone number already being used!");
                    }
                    else {
                        db.insertData(inputFirstName + " " + inputLastName, null, inputPhoneNumber, inputNCCID, null, null, null, null, null);
                        NavHostFragment.findNavController(FirstFormFragment.this)
                                .navigate(R.id.action_DBR_FirstFormFragment_to_SecondFormFragment);
                    }
                     */

                    NavHostFragment.findNavController(FirstFormFragment.this)
                            .navigate(R.id.action_DBR_FirstFormFragment_to_SecondFormFragment);
                }
            }
        });

//        // Creating the database and passing the correct context as the argument
//        db = new GuestRegistrySource(requireContext());
//
//        view.findViewById(R.id.next_button_first_fragment_gRegistration).setOnClickListener(v ->
//        {
//
//            // Getting a handle on info from the UI
//            // TODO: line #108
//            // name = view.findViewById(R.id.editText);
//            // phone = view.findViewById(R.id.editText2);
//            // email = view.findViewById(R.id.editText3);
//
//            inputFirstName = ((EditText) getView().findViewById(R.id.guestreg_fname)).getText().toString();
//            inputLastName = ((EditText) getView().findViewById(R.id.guestreg_lname)).getText().toString();
//            inputPhoneNumber = ((EditText) getView().findViewById(R.id.guestreg_phone)).getText().toString();
//            inputNCCID = ((EditText) getView().findViewById(R.id.guestreg_nccid)).getText().toString();
//
//            /*
//            firstName = view.findViewById(R.id.guestreg_fname);
//            lastName = view.findViewById(R.id.guestreg_lname);
//            phoneNumber = view.findViewById(R.id.guestreg_phone);
//            nccID = view.findViewById(R.id.guestreg_nccid);
//
//            firstName.getText().toString();
//            lastName.getText().toString();
//            phoneNumber.getText().toString();
//            nccID.getText().toString();
//
//             */
//
//            Log.d("**", firstName.getText().toString());
//
//            if(inputFirstName.length() > 0 &&
//                    inputLastName.length() > 0 &&
//                    inputPhoneNumber.length() == 10 &&
//                    inputNCCID.length() == 8) {
//
//                Bundle bundle = new Bundle();
//
//                bundle.putString("First Name", inputFirstName);
//                bundle.putString("Last Name", inputLastName);
//                bundle.putString("Phone Number", inputPhoneNumber);
//                bundle.putString("NCC ID", inputNCCID);
//
//                getParentFragmentManager().setFragmentResult("sending_first_fragment_result", bundle);
//
//                Log.d("**", "in here");
//
////                NavHostFragment.findNavController(FirstFormFragment.this)
////                        .navigate(R.id.action_DBR_FirstFormFragment_to_SecondFormFragment);
//            }
//            else{
//                Log.d("**", "in here else");
//            }




//            // For testing purposes
//            Log.d(TAG, "The name is: " + inputName);
//            Log.d(TAG, "The phone is: " + inputPhone);
//            Log.d(TAG, "The email is: " + inputEmail);
//
//            // TODO: When fragments updated by the UI team, use bindings to store the rest of the inputs
//            String inputId = id.getText().toString();
//            String inputAddress = address.getText().toString();
//            String inputCity = city.getText().toString();
//            String inputZip = zip.getText().toString();
//            String inputDate = date.getText().toString();
//
//            Bundle bundle = new Bundle();
//
//            bundle.putString("NAME", inputName);
//            bundle.putString("PHONE", inputPhone);
//            bundle.putString("EMAIL", inputEmail);
//            bundle.putString("ID", inputId);
//            bundle.putString("ADDRESS", inputAddress);
//            bundle.putString("CITY", inputCity);
//            bundle.putString("ZIP", inputZip);
//            bundle.putString("DATE", inputDate);

            // TODO Set the bundle as the FragmentResult for the next fragment to retrieve

            // Navigate to the SecondFormFragment
//            NavHostFragment.findNavController(FirstFormFragment.this)
//                    .navigate(R.id.action_DBR_FirstFormFragment_to_SecondFormFragment);

//        });

    }


//    /**
//     * onClick --
//     * Submits user-data when click is received.
//     * Notifies user in a toast if the adding is successful
//     *
//     * @param view The view that was clicked
//     */
//    @Override
//    public void onClick(View view) {
//
//        // TODO This method may be getting replaced by sending the data to the next fragment instead.
//        // If the data is being passed through fragments, you may need to move this method to the
//        // last fragment. See onViewCreated()
//
//        // Variable used for checks
//        boolean ins = false;
//
//        // Adding the values into the database if submit button is pressed
//        if (view.getId() == R.id.next_button_first_fragment_gRegistration) {
//
//
//
//            // NOTE: The parameter 'barcode' was recently added to this method
//            // TODO: Update parameter 'barcode' to the barcode representing this user
//            //ins = db.insertData(name.getText().toString(), email.getText().toString(), phone.getText().toString(), date.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), null);
//
//        }
//
//        // Notifying the user if the add was successful
//        if (ins) {
//
//            // For testing purposes, comment out if not needed.
//            Toast.makeText(requireContext(), "User Added", Toast.LENGTH_LONG).show();
//
//        }
//
//    }
//
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//    {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent)
//    {
//
//    }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
