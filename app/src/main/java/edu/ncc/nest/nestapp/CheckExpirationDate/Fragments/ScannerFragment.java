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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.AbstractScanner.AbstractScannerFragment;
import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * ScannerFragment: Used to scan in a UPC barcode, and send it to the appropriate fragment depending
 * on whether or not a product with the scanned barcode exist in the database.
 *
 * Navigates to {@link ConfirmItemFragment} with the item pulled from database, if the upc exists in
 * the local database.
 *
 * Navigates to {@link SelectItemFragment} with the barcode, if the upc does not exist in the local
 * database.
 */
public class ScannerFragment extends AbstractScannerFragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = ScannerFragment.class.getSimpleName();

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear any set fragment results since they are not needed in or prior to this fragment
        getParentFragmentManager().clearFragmentResult("FOOD ITEM");

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.UPC_A);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        // Log the barcode result and format
        Log.d(LOG_TAG, "Scan Confirmed: [" + barcode + ", " + format.toString() + "]");

        NestDBDataSource dataSource = CheckExpirationDateActivity.requireDataSource(this);

        FragmentManager fragmentManager = getParentFragmentManager();

        // Find the NestUPC object that matches the scanned barcode
        NestUPC foodItem = dataSource.getNestUPC(barcode);

        Bundle result = new Bundle();

        if (foodItem != null) {

            // NOTE: If we get here, then the upc is already in the database.

            // Put the food item into the bundle and pass it to ConfirmItemFragment
            result.putSerializable("foodItem", foodItem);

            fragmentManager.setFragmentResult("FOOD ITEM", result);

            // Navigate to ConfirmItemFragment
            NavHostFragment.findNavController(ScannerFragment.this)
                    .navigate((R.id.CED_ConfirmItemFragment));

        } else {

            // NOTE: If we get here, then the upc is does not exist in the database.

            // Put UPC into the bundle
            result.putString("upcBarcode", barcode);

            fragmentManager.setFragmentResult("FOOD ITEM", result);

            // Navigate to SelectItemFragment
            NavHostFragment.findNavController(ScannerFragment.this)
                    .navigate((R.id.CED_SelectItemFragment));

        }

    }

}