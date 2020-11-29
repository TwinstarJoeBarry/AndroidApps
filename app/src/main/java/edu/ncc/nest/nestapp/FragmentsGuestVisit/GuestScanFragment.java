package edu.ncc.nest.nestapp.FragmentsGuestVisit;

/**     Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 * ========================================================================
 * jai-imageio
 * ========================================================================
 *
 * Copyright (c) 2005 Sun Microsystems, Inc.
 * Copyright © 2010-2014 University of Manchester
 * Copyright © 2010-2015 Stian Soiland-Reyes
 * Copyright © 2015 Peter Hull
 * All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for
 * use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.ViewfinderView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import edu.ncc.nest.nestapp.R;
import me.dm7.barcodescanner.core.CameraUtils;

/**
 * GuestScanFragment - Fragment to be used to check in a user by scanning a guest's bar code that
 * was given to them at registration time.
 */
public class GuestScanFragment extends Fragment implements BarcodeCallback, View.OnClickListener {

    private static final String TAG = GuestScanFragment.class.getSimpleName();

    private static final List<BarcodeFormat> BARCODE_FORMATS = Collections.singletonList(BarcodeFormat.CODE_39);
    private static final int CAMERA_REQ_CODE = 250; // Camera permission request code
    private static final long SCAN_DELAY = 2000L;   // 2 Seconds decoder delay in milliseconds

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private TextView resultTextView;

    private boolean askedForPermission = false;
    private boolean scannerPaused = true;

    // Stores the bar code that has been scanned
    private String barcodeResult = null;


    ////////////// Lifecycle Methods Start //////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_scan, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get respective views from layout
        barcodeView = (DecoratedBarcodeView) view.findViewById(R.id.zxing_barcode_scanner);

        ViewfinderView viewfinderView = (ViewfinderView) view.findViewById(R.id.zxing_viewfinder_view);

        Button confirmButton = (Button) view.findViewById(R.id.confirm_scan_button);

        Button rescanButton = (Button) view.findViewById(R.id.rescan_button);

        resultTextView = (TextView) view.findViewById(R.id.scan_result_textview);


        // Specifies which barcode formats to decode
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(BARCODE_FORMATS));

        // Favor back-facing camera. If none exists, fallback to whatever camera is available
        barcodeView.getCameraSettings().setRequestedCameraId(CameraUtils.getDefaultCameraId());


        // Scanner customization
        viewfinderView.setMaskColor(Color.argb(100, 0, 0, 0));

        viewfinderView.setLaserVisibility(true);


        // Assign OnClickListener as this class
        confirmButton.setOnClickListener(this);

        rescanButton.setOnClickListener(this);


        // Create new BeepManager object
        beepManager = new BeepManager(requireActivity());

        // Enable vibration and beep to play when a bar-code is scanned
        beepManager.setVibrateEnabled(true);

        beepManager.setBeepEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (cameraPermissionGranted())

            resumeScanning();

        else if (!askedForPermission) {

            // Request the camera permission to be granted
            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_REQ_CODE);

            // We have officially asked for permission, so update our class variable
            askedForPermission = true;

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        // Since we have paused the fragment pause and wait for the camera to close
        barcodeView.pauseAndWait();

        scannerPaused = true;

    }

    @Override
    public void onDestroy() {

        // Since we are destroying the fragment pause and wait for the camera to close
        barcodeView.pauseAndWait();

        scannerPaused = true;

        super.onDestroy();

    }


    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CAMERA_REQ_CODE && grantResults.length > 0)

            // If we have permission to use the camera
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)

                resumeScanning();

            else

                // Display a reason of why we need the permission
                Toast.makeText(requireActivity(), "Camera permission is needed in order to scan.",
                        Toast.LENGTH_SHORT).show();

    }

    @Override
    public void barcodeResult(BarcodeResult result) {

        // Gets the barcode from the result
        String resultText = result.getText();

        // Make sure we actually have a barcode scanned
        if (resultText != null) {

            // Play a sound and vibrate when a scan has been processed
            beepManager.playBeepSoundAndVibrate();

            // Display the barcode back to the user
            resultTextView.setText(resultText);

            // Store the barcode
            barcodeResult = resultText;

            // Pause the scanner
            barcodeView.pause();

            scannerPaused = true;

            Log.d(TAG, "Barcode Result: " + resultText + ", Barcode Format: " + result.getBarcodeFormat());

        } else

            // Scan for another bar-code
            barcodeView.decodeSingle(GuestScanFragment.this);

    }

    @Override
    public void onClick(View view) {

        if (cameraPermissionGranted()) {

            int id = view.getId();

            if (id == R.id.rescan_button)

                resumeScanning();

            else if (id == R.id.confirm_scan_button && barcodeResult != null) {

                Log.d(TAG, "Scan Confirmed: " + barcodeResult);

                // TODO Create bundle and send barcode with guest name to next fragment

            }

        } else

            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQ_CODE);

    }


    ////////////// Custom Methods Start  //////////////

    /**
     * cameraPermissionGranted - Takes no parameters
     * @return Returns true if camera permission has been granted or false otherwise.
     * Description: Determines if camera permission has been granted.
     */
    private boolean cameraPermissionGranted() {

        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED);

    }

    /**
     * resumeScanning - Takes no parameters
     * Description: Resumes the scanner if it is not paused, gets the current system time, and resets
     * the barcodeResult to be null so we can scan a new bar-code.
     */
    private void resumeScanning() {

        if (scannerPaused) {

            // Update the display text so the user knows we are waiting for them to scan a barcode
            resultTextView.setText(getString(R.string.scan_result_textview));

            // Reset our barcodeResult
            barcodeResult = null;

            // Resume the scanner but not the decoder
            barcodeView.resume();

            scannerPaused = false;

            // Create a handler that resumes the decoder after a delay
            // Gives the user time to move their camera before scanning
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                if (!scannerPaused)

                    // Resume decoding after a delay of SCAN_DELAY millis as long as the scanner has not been paused
                    // Tells the decoder to stop after a single scan
                    barcodeView.decodeSingle(GuestScanFragment.this);

            }, SCAN_DELAY);

        }

    }

}