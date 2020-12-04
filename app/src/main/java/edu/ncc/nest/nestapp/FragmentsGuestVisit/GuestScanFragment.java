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
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.*;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collections;
import java.util.List;

import edu.ncc.nest.nestapp.GuestFormHelper;
import edu.ncc.nest.nestapp.R;

/**
 * GuestScanFragment - Fragment to be used to check in a user by scanning a guest's bar code that
 * was given to them at registration time.
 */
public class GuestScanFragment extends Fragment implements BarcodeCallback, View.OnClickListener {

    public static final String TAG = GuestScanFragment.class.getSimpleName();

    private static final List<BarcodeFormat> BARCODE_FORMATS = Collections.singletonList(BarcodeFormat.CODE_39);
    // To support multiple formats change this to Arrays.asList() and fill it with the required
    // formats. For example, Arrays.asList(BarcodeFormat.CODE_39, BarcodeFormat.UPC_A, ...);

    // Used to ask for camera permission calls cameraPermissionResult method with the result
    private final ActivityResultLauncher<String> REQUEST_PERMISSION_LAUNCHER = registerForActivityResult(
            new RequestPermission(), this::onCameraPermissionResult);

    private static final long SCAN_DELAY = 1500L;   // 2 Seconds decoder delay in milliseconds

    private DecoratedBarcodeView decBarcodeView;
    private BeepManager beepManager;
    private TextView resultTextView;
    private Button confirmButton;
    private Button rescanButton;

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
        decBarcodeView = (DecoratedBarcodeView) view.findViewById(R.id.zxing_barcode_scanner);

        confirmButton = (Button) view.findViewById(R.id.confirm_scan_button);

        rescanButton = (Button) view.findViewById(R.id.rescan_button);

        resultTextView = (TextView) view.findViewById(R.id.scan_result_textview);


        // Specifies which barcode formats to decode. (Removing this line, defaults scanner to use all formats)
        decBarcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(BARCODE_FORMATS));


        // Make this class the OnClickListener for both feedback buttons
        confirmButton.setOnClickListener(this);

        rescanButton.setOnClickListener(this);

        // Disable the feedback buttons until we scan a barcode
        setFeedbackButtonsEnabled(false);


        // Create new BeepManager object to handle beeps and vibration
        beepManager = new BeepManager(requireActivity());

        beepManager.setVibrateEnabled(true);

        beepManager.setBeepEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();

        // If the camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            resumeScanning();

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

            // TODO Create a dialog window describing why we need the permission for this feature

            // Display a reason of why we need the permission
            Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                    Toast.LENGTH_LONG).show();

        } else

            // Request the camera permission
            REQUEST_PERMISSION_LAUNCHER.launch(Manifest.permission.CAMERA);

    }

    @Override
    public void onPause() {
        super.onPause();

        // Since we have paused the fragment pause and wait for the camera to close
        decBarcodeView.pauseAndWait();

        scannerPaused = true;

    }

    @Override
    public void onDestroy() {

        // Make sure we have the view in-case the view isn't initialized before destruction
        if (decBarcodeView != null) {

            // Since we are destroying the fragment pause and wait for the camera to close
            decBarcodeView.pauseAndWait();

            scannerPaused = true;

        }

        super.onDestroy();

    }


    ////////////// Implementation Methods Start  //////////////

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
            decBarcodeView.pause();

            scannerPaused = true;

            // Enable the feedback buttons after we have stored the bar-code, and stopped scanner
            setFeedbackButtonsEnabled(true);

            Log.d(TAG, "Barcode Result: " + resultText + ", Barcode Format: " + result.getBarcodeFormat());

        } else

            // Scan for another bar-code
            decBarcodeView.decodeSingle(GuestScanFragment.this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.rescan_button)

            resumeScanning();

        else if (id == R.id.confirm_scan_button && barcodeResult != null) {

            Log.d(TAG, "Scan Confirmed: " + barcodeResult);

            // Create the result
            Bundle resultBundle = new Bundle();

            // Put the barcodeResult into the bundle
            resultBundle.putString("BARCODE", barcodeResult);

            // Create an instance of the database helper
            GuestFormHelper db = new GuestFormHelper(requireContext());

            // Check if the guest is registered in the database
            final String GUEST_NAME = db.isRegistered(barcodeResult);
            
            if (GUEST_NAME != null)

                // If the guest is registered, include the guest's name in the result
                resultBundle.putString("GUEST_NAME", GUEST_NAME);

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("CONFIRM_SCAN", resultBundle);

            // NOTE: The following code is only temporary and is for testing the GuestQuestionnaireFragment
            // TODO Replace this with code that will navigate to the confirmation fragment
            NavHostFragment.findNavController(GuestScanFragment.this)
                    .navigate(R.id.action_GuestScanFragment_to_GuestQuestionnaireFragment);

        }

    }


    ////////////// Custom Methods Start  //////////////

    /**
     * Takes 1 parameter. This method gets called by the requestPermissionLauncher, after asking for
     * permissions. Determines what happens when the permission gets granted or denied.
     * @param isGranted - true if permission was granted false otherwise
     */
    private void onCameraPermissionResult(boolean isGranted) {

        if (isGranted)

            // Permission is granted so resume scanning
            resumeScanning();

        else

            // Display a reason of why we need the permission
            Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                    Toast.LENGTH_LONG).show();

    }

    /**
     * Takes 0 parameters. Resumes the scanner if it is not paused, resets text view text, resets
     * the barcodeResult to be null so we can scan a new bar-code, and starts the decoder after a
     * delay.
     */
    private void resumeScanning() {

        if (scannerPaused) {

            // Update the display text so the user knows we are waiting for them to scan a barcode
            resultTextView.setText(getString(R.string.scan_result_textview));

            // Disable the feedback buttons until we scan another barcode
            setFeedbackButtonsEnabled(false);

            // Reset our barcodeResult
            barcodeResult = null;

            // Resume the scanner but not the decoder
            decBarcodeView.resume();

            scannerPaused = false;

            // Create a handler that resumes the decoder after a delay
            // Gives the user time to move their camera before scanning
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                if (!scannerPaused)

                    // Resume decoding after a delay of SCAN_DELAY millis as long as the scanner has not been paused
                    // Tells the decoder to stop after a single scan
                    decBarcodeView.decodeSingle(GuestScanFragment.this);

            }, SCAN_DELAY);

        }

    }

    /**
     * Takes 1 parameter. Toggles whether both rescanButton and confirmScan button are enabled or
     * disabled based on the value of the parameter.
     *
     * @param enabled true to enable or false disable
     */
    private void setFeedbackButtonsEnabled(boolean enabled) {

        confirmButton.setEnabled(enabled);

        rescanButton.setEnabled(enabled);

    }

}