package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

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
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.AbstractScanner.AbstractScannerFragment;
import edu.ncc.nest.nestapp.R;

/**
 * ScannerFragment: Scans in a barcode provided by the guest using the camera. It then checks
 * whether or not the barcode is already registered to a guest in the local registry database. If
 * the barcode is not currently registered to a guest, it then sends the barcode to
 * {@link FirstFormFragment}. If the barcode is already in use, it should then ask the guest if they
 * want to re-scan another barcode or go to the GuestVisit feature
 * {@link edu.ncc.nest.nestapp.GuestVisit.Activities.GuestVisitActivity}.
 */
public class ScannerFragment extends AbstractScannerFragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = edu.ncc.nest.nestapp.CheckExpirationDate.Fragments.ScannerFragment.class.getSimpleName();

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear any set fragment results since they are not needed in or prior to this fragment
        //getParentFragmentManager().clearFragmentResult("FOOD ITEM");

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.CODE_39);
    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        // leaving below code about FragmentManager. probably a way to make a global bundle.
        // FragmentManager fragmentManager = getParentFragmentManager();
        // just take it and pass it. doesn't check if in database already.
        Bundle result = new Bundle();

        // Put UPC into the bundle
        result.putString("registrationBarcode", barcode);

        // i think this is sending to a global bundle?
        // fragmentManager.setFragmentResult("FOOD ITEM", result);

        Toast toast = Toast.makeText(getContext(), "Confirmed! Barcode is: " + barcode, Toast.LENGTH_SHORT);
        toast.show();
        // Navigate to NextFragment
        /*
        NavHostFragment.findNavController(edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments.ScannerFragment.this)
                .navigate((R.id.CED_SelectItemFragment));

         */
    }

}
