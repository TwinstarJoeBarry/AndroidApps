package edu.ncc.nest.nestapp.FragmentsGuestVisit;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.FragmentScanner.ScannerFragment;
import edu.ncc.nest.nestapp.GuestFormHelper;
import edu.ncc.nest.nestapp.R;

/**
 * GuestScanFragment - Fragment to be used to check in a user by scanning a guest's bar code that
 * was given to them at registration time.
 */
public class GuestScanFragment extends ScannerFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.debugClass = GuestScanFragment.class;

        super.debug = true;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        super.setDecoderFormats(BarcodeFormat.CODE_39);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        // Create the Bundle that will be used to send the barcode to the next fragment
        Bundle resultBundle = new Bundle();

        // Put the barcodeResult into the bundle
        resultBundle.putString("BARCODE", barcode);

        // Create an instance of the database helper
        GuestFormHelper db = new GuestFormHelper(requireContext());

        // Check if the guest is registered in the database
        // No guests yet so this will always be null. GUEST_NAME was set to "Test" for testing purposes.
        //final String GUEST_NAME = db.isRegistered(barcodeResult);
        //TODO: Replace the line below with the one above.
        final String GUEST_NAME = "Test";

        if (GUEST_NAME != null)

            // If the guest is registered, include the guest's name in the result
            resultBundle.putString("GUEST_NAME", GUEST_NAME);

        // Set the fragment result to the bundle
        getParentFragmentManager().setFragmentResult("SCAN_CONFIRMED", resultBundle);

        // Navigate to the confirmation fragment
        NavHostFragment.findNavController(GuestScanFragment.this)
                .navigate(R.id.action_GuestScanFragment_to_GuestScanConfirmationFragment);

    }

}