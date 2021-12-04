package edu.ncc.nest.nestapp.GuestVisit.Fragments;

/**
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
 *
 * Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.AbstractScanner.AbstractScannerFragment;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

/**
 * ScannerFragment: Fragment to be used to check in a user by scanning a guest's bar code that was
 * given to them at registration time.
 */
public class ScannerFragment extends AbstractScannerFragment {

    public static final String TAG = ScannerFragment.class.getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.CODE_39);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        Log.d(TAG, "Scan Confirmed: [" + barcode + ", " + format.toString() + "]");

        // Create the Bundle that will be used to send the barcode to the next fragment
        Bundle resultBundle = new Bundle();

        // Put the barcodeResult into the bundle
        resultBundle.putString("BARCODE", barcode);

        // Create an instance of the database helper
        GuestRegistrySource db = new GuestRegistrySource(requireContext());

        //////////////////////////////////// TESTING CODE START ////////////////////////////////////

        /**
         * TODO: This code is for testing purposes only and should be removed/commented-out when not testing.
         *
         * Uncomment the following code to add a new guest to the GuestRegistry database with the
         * current barcode. This makes sure that a guest associated with the currently scanned
         * barcode will be found in the GuestRegistry. This allows you to bypass having to register
         * with the 'GuestDatabaseRegistration' feature until it is properly set up.
         *
         * When this code is uncommented, you should be brought to ConfirmationFragment and see the
         * title of "Welcome NEST Guest!", and a "Guest Name" of "John Doe" and a "Guest ID" equal
         * to the currently scanned barcode. Press 'Confirm' to be taken to the
         * QuestionnaireFragment. The QuestionnaireFragment should auto-fill the first field for you
         * with the currently scanned barcode.
         *
         * In the QuestionnaireFragment, fill in all the empty fields and press "Submit". If there
         * are any missing fields, the form will not be submitted and should display a dialog
         * informing you of the missing fields. If all fields have been entered, the form should be
         * submitted to the Questionnaire database. QuestionnaireFragment will then print all the
         * submissions submitted by the guest that is associated with the current barcode to the log
         * from the Questionnaire database.
         *
         * See issue #181 on GitLab for more info about QuestionnaireFragment.
         *
         * You can change each parameter to whatever you want. If you want a specific barcode to be
         * associated with this guest, just change the last parameter 'barcode' to the barcode that
         * you want associated with this guest.
         */

        // If a guest associated with the currently scanned barcode does not exist in the database
        /*if (db.isRegistered(barcode) == null) {

            db.open();

            // NOTE: This method may change over time, make sure it is up to date with GuestRegistrySource.
            if (!db.insertData("John Doe", "John.Doe@example.com", "555-555-5555", "01/23/45",
                    "123 Test St", "Test", "12345", barcode))

                Log.e(TAG, "Error adding \"testing\" guest to registry.");

            db.close();

        } else

            Log.w(TAG, "Did not add new guest because a guest already exists with that barcode");
         */

        ///////////////////////////////////// TESTING CODE END /////////////////////////////////////

        // TODO 'GuestDatabaseRegistration' feature is not finished yet so this might always be null
        // Check if the guest is registered in the database
        final String GUEST_NAME = db.isRegistered(barcode);

        if (GUEST_NAME != null)

            // If the guest is registered, include the guest's name in the result
            resultBundle.putString("GUEST_NAME", GUEST_NAME);

        // Set the fragment result to the bundle
        getParentFragmentManager().setFragmentResult("SCAN_CONFIRMED", resultBundle);

        // Navigate to the confirmation fragment
        NavHostFragment.findNavController(ScannerFragment.this)
                .navigate(R.id.action_GV_ScannerFragment_to_ConfirmationFragment);

    }

}