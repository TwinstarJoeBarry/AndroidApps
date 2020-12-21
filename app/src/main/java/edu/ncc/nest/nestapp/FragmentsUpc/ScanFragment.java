package edu.ncc.nest.nestapp.FragmentsUpc;

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

import edu.ncc.nest.nestapp.FragmentScanner.ScannerFragment;
import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class ScanFragment extends ScannerFragment {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.UPC_A);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        Log.d(TAG, "Scan Confirmed: [" + barcode + ", " + format.toString() + "]");

        // Check database
        NestDBDataSource dataSource = new NestDBDataSource(getContext());
        NestUPC result = dataSource.getNestUPC(barcode);

        // Used this to test if there was a non-null result given (successful)
        /*result = new NestUPC("123456789123", "Hershey's", "Chocolate Bar",
                123, "Hershey's Chocolate Bar", null,
                1234,"Some category description");*/

        // If there is a result from the database
        if(result != null) {

            Log.d(TAG, "Result returned: " + result.getUpc() + " " + result.getProductName());

            // Put the item in a bundle and pass it to ConfirmItemFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("foodItem", result);
            getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);
            NavHostFragment.findNavController(ScanFragment.this).navigate((R.id.confirmItemFragment));

        // If there was no result from the database
        } else {

            // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
            Bundle bundle = new Bundle();
            bundle.putString("barcode", barcode);
            getParentFragmentManager().setFragmentResult("BARCODE", bundle);
            NavHostFragment.findNavController(ScanFragment.this).navigate((R.id.selectItemFragment));

        }

    }

}