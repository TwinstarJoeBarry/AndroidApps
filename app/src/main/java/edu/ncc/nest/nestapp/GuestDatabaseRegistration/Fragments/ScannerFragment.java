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
import android.view.View;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.AbstractScannerFragment.AbstractScannerFragment;

/**
 * ScannerFragment: Scans in a barcode provided by the guest using the camera. It then checks
 * whether or not the barcode is already registered to a guest in the local registry database. If
 * the barcode is not currently registered to a guest, it then sends the barcode to
 * {@link FirstFormFragment}. If the barcode is already in use, it should then ask the guest if they
 * want to re-scan another barcode or go to the GuestVisit feature
 * {@link edu.ncc.nest.nestapp.GuestVisit.Activities.GuestVisitActivity}.
 */
public class ScannerFragment extends AbstractScannerFragment {

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.CODE_39);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

    }

}
