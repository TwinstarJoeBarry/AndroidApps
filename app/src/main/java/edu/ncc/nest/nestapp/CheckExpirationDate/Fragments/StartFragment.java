package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2020 The LibreFoodPantry Developers.
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

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * StartFragment: This is the starting fragment for the CheckExpirationDate feature. This fragment
 * should ask the user whether or not they want to scan or enter a UPC barcode.
 *
 * Navigates to {@link ScannerFragment} when "Scan UPC" is selected.
 *
 * Navigates to {@link EnterUpcFragment} when "Enter UPC Manually" is selected.
 */
public class StartFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    public static final String LOG_TAG = StartFragment.class.getSimpleName();

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear any set fragment results since they are not needed in or prior to this fragment
        getParentFragmentManager().clearFragmentResult("FOOD ITEM");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_start,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EditText) view.findViewById(R.id.upc_entry)).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ImageView alertImg = view.findViewById(R.id.alert_image);

                if (s.length() == 12) {

                    if (s.toString().matches("[0-9]+")) {

                        alertImg.setImageResource(R.drawable.ic_check_mark);

                        alertImg.setVisibility(View.VISIBLE);

                    } else {

                        alertImg.setImageResource(R.drawable.ic_error);

                        alertImg.setVisibility(View.VISIBLE);

                    }

                } else

                    alertImg.setVisibility(View.GONE);

            }

        });

        view.findViewById(R.id.upc_entry).setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                // Hide the keyboard and clear focus from the EditText
                hideSoftInputFromWindow();

                return true;

            } else

                return false;

        });

        // Set the OnClickListener for start_scan_btn
        view.findViewById(R.id.start_scan_btn).setOnClickListener(view1 -> {

            // Hide the keyboard and clear focus from the EditText
            hideSoftInputFromWindow();

            // Navigate to ScannerFragment
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.CED_ScannerFragment);

        });

        // Set the OnClickListener for start_enter_btn
        view.findViewById(R.id.start_enter_btn).setOnClickListener(v -> {

            // Hide the keyboard and clear focus from the EditText
            hideSoftInputFromWindow();

            // Look in the EditText widget and retrieve the String the user passed in
            EditText editText = view.findViewById(R.id.upc_entry);

            // Get the upc string from the EditText object
            String upcBarcode = editText.getText().toString();

            // Check validity of the UPC
            if (upcBarcode.length() != 12 || !upcBarcode.matches("[0-9]+")) {

                Toast.makeText(getContext(),
                        "Entry invalid! Please enter a 12-digit number.",
                        Toast.LENGTH_SHORT).show();

                ImageView alertImg = view.findViewById(R.id.alert_image);

                alertImg.setImageResource(R.drawable.ic_error);

                alertImg.setVisibility(View.VISIBLE);

            } else {

                NestDBDataSource dataSource =
                        CheckExpirationDateActivity.requireDataSource(this);

                FragmentManager fragmentManager = getParentFragmentManager();

                NestUPC foodItem = dataSource.getNestUPC(upcBarcode);

                Bundle result = new Bundle();

                if (foodItem != null) {

                    // NOTE: If we get here, then the upc is already in the database.

                    // Put the item in a bundle and pass it to ConfirmItemFragment
                    result.putSerializable("foodItem", foodItem);

                    fragmentManager.setFragmentResult("FOOD ITEM", result);

                    NavHostFragment.findNavController(StartFragment.this)
                            .navigate((R.id.CED_ConfirmItemFragment));

                } else {

                    // NOTE: If we get here, then the upc is does not exist in the database.

                    // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
                    result.putString("upcBarcode", upcBarcode);

                    fragmentManager.setFragmentResult("FOOD ITEM", result);

                    NavHostFragment.findNavController(StartFragment.this)
                            .navigate((R.id.CED_SelectItemFragment));

                }

            }

        });

    }

    /**
     * Request to hide the soft-input-window/keyboard from the context of the window that is
     * currently accepting input.
     */
    private void hideSoftInputFromWindow() {

        requireView().findViewById(R.id.upc_entry).clearFocus();

        ((InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(requireView().getRootView().getApplicationWindowToken(),
                        0);

    }

}