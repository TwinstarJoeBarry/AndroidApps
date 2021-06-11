package edu.ncc.nest.nestapp.AbstractScanner;

/*
 * Copyright (C) 2020-2021 The LibreFoodPantry Developers.
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

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import edu.ncc.nest.nestapp.R;

/**
 * Abstract Fragment class that handles the scanning of bar-codes.
 */
@SuppressWarnings("unused")
public abstract class AbstractScannerFragment extends Fragment implements BarcodeCallback {

    public static final String LOG_TAG = AbstractScannerFragment.class.getSimpleName();

    // Used to ask for camera permission. Calls onCameraPermissionResult method with the result
    private final ActivityResultLauncher<String> REQUEST_CAMERA_PERMISSION_LAUNCHER =
            registerForActivityResult(new RequestPermission(), this::onCameraPermissionResult);

    // Delay the decoder on resume for 1.5 Seconds in milliseconds
    private static final long DECODER_DELAY = 1500L;

    private DecoratedBarcodeView decBarcodeView;
    private BeepManager beepManager;
    private TextView resultTextView;
    private Button confirmButton;
    private Button rescanButton;

    private boolean scannerPaused = true;

    private BarcodeFormat barcodeFormat;
    private String barcodeText;


    //////////////////////////////////// Abstract Methods Start ////////////////////////////////////

    protected abstract void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format);


    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.abstract_scanner, container, false);

    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get respective views from layout
        decBarcodeView = view.findViewById(R.id.scanner_decorated_barcode_view);

        resultTextView = view.findViewById(R.id.scanner_result_view);

        confirmButton = view.findViewById(R.id.scanner_confirm_button);

        rescanButton = view.findViewById(R.id.scanner_rescan_button);


        // Set the onClick listeners to call the respective method onClick
        rescanButton.setOnClickListener(v -> resumeScanning());

        confirmButton.setOnClickListener(v -> {

            setFeedbackButtonsEnabled(false);

            onBarcodeConfirmed(barcodeText, barcodeFormat);

        });

        // Disable the feedback buttons until we scan a barcode
        setFeedbackButtonsEnabled(false);


        // Create new BeepManager object to handle beeps and vibration
        beepManager = new BeepManager(requireActivity());

        beepManager.setVibrateEnabled(true);

        beepManager.setBeepEnabled(true);


        // If camera permission is not granted, request the camera permission to be granted
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Update the status text to inform the guest that camera permission is required
            decBarcodeView.setStatusText(getString(R.string.abstract_scanner_fragment_camera_permission_required));

            // Clear the result text view
            resultTextView.setText(null);

            // If we should show a request permission rationale for the camera permission
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                /* Create a AlertDialog that informs the user that the camera permission needs to be
                 * granted in order to use this feature */
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                // Allow the user one more chance to accept the permission from within the app
                builder.setPositiveButton("OK", (dialog, which) -> {

                    dialog.dismiss();

                    // Request the camera permission
                    REQUEST_CAMERA_PERMISSION_LAUNCHER.launch(Manifest.permission.CAMERA);

                });

                /* Include a "cancel" or "no thanks" button that allows the user to continue using
                 * the app without granting the permission. */
                builder.setNeutralButton("No thanks", (dialog, which) -> dialog.dismiss());

                builder.setMessage("Camera permission is needed in order to scan.");

                builder.setTitle("Permission Required");

                builder.create().show();

            } else

                // Request the camera permission
                REQUEST_CAMERA_PERMISSION_LAUNCHER.launch(Manifest.permission.CAMERA);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // If camera permission is granted, then resume scanning
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

            resumeScanning();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (!scannerPaused) {

            // Since we have paused the fragment, pause and wait for the camera to close
            decBarcodeView.pauseAndWait();

            scannerPaused = true;

        }

    }

    @Override
    public void onDestroy() {

        // Make sure we have the view in-case the view isn't initialized before destruction
        if (!scannerPaused) {

            // Since we are destroying the fragment, pause and wait for the camera to close
            decBarcodeView.pauseAndWait();

            scannerPaused = true;

        }

        super.onDestroy();

    }


    ////////////////////////////////// Other Event Methods Start  //////////////////////////////////

    @Override
    public final void barcodeResult(BarcodeResult result) {

        // Gets the barcode from the result
        String resultText = result.getText();

        // Make sure we actually have a barcode scanned
        if (resultText != null) {

            // Pause the scanner
            decBarcodeView.pause();

            scannerPaused = true;

            // Play a sound and vibrate when a scan has been processed
            beepManager.playBeepSoundAndVibrate();

            // Tell the user to confirm that the barcode is correct
            decBarcodeView.setStatusText(getString(R.string.abstract_scanner_fragment_confirm_msg));

            // Display the barcode back to the user
            resultTextView.setText(resultText);

            // Store the barcode format
            barcodeFormat = result.getBarcodeFormat();

            // Store the barcode
            barcodeText = resultText;

            // Enable the feedback buttons after we have stored the bar-code and stopped scanner
            setFeedbackButtonsEnabled(true);

        } else

            // Scan for another bar-code
            decBarcodeView.decodeSingle(AbstractScannerFragment.this);

    }

    @Override // Made this method final so it can't be overridden
    public final void possibleResultPoints(List<ResultPoint> resultPoints) { }


    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * onCameraPermissionResult -- Takes 1 parameter.
     * This method gets called by the REQUEST_CAMERA_PERMISSION_LAUNCHER, after
     * asking for camera permission. Determines what happens when the permission gets granted or
     * denied.
     * @param cameraPermissionGranted - true if permission was granted false otherwise
     */
    private void onCameraPermissionResult(boolean cameraPermissionGranted) {

        if (!cameraPermissionGranted) {

            /* Explain to the user that the feature is unavailable because the feature requires a
             * permission that the user has denied. Respect the user's decision. DON'T link to
             * system settings in an effort to convince the user to change their decision. */

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setNeutralButton("Dismiss", (dialog, which) -> dialog.dismiss());

            builder.setMessage("Camera permission is needed in order to scan.");

            builder.setTitle("Permission Required");

            builder.create().show();

        } else

            // Camera permission is granted, so resume scanning
            resumeScanning();

    }

    /**
     * resumeScanning -- Takes 0 parameters.
     * Resumes the scanner if it is not paused, resets resultTextView text,
     * resets the barcodeResult to be null so we can scan a new bar-code, and starts the decoder
     * after a delay of SCAN_DELAY.
     */
    private void resumeScanning() {

        if (scannerPaused) {

            // Update the status text to explain how to use the scanner
            decBarcodeView.setStatusText(getString(R.string.zxing_msg_default_status));

            // Update the display text so the user knows we are waiting for them to scan a barcode
            resultTextView.setText(getString(R.string.abstract_scanner_fragment_waiting_for_scan));

            // Disable the feedback buttons until we scan another barcode
            setFeedbackButtonsEnabled(false);

            // Reset our barcodeText and format
            barcodeText = null;

            barcodeFormat = null;

            // Resume the scanner but not the decoder
            decBarcodeView.resume();

            scannerPaused = false;

            // Create a handler that resumes the decoder after a delay
            // Gives the user time to move their camera before scanning
            new Handler(Looper.myLooper()).postDelayed(() -> {

                // As long as the scanner hasn't been paused, start the decoder
                if (!scannerPaused)

                    // Tells the decoder to stop after a single scan
                    decBarcodeView.decodeSingle(AbstractScannerFragment.this);

            }, DECODER_DELAY);

        }

    }

    /**
     * setFeedbackButtonsEnabled -- Takes 1 parameter.
     * Toggles whether both rescanButton and confirmScan button are enabled or
     * disabled, based on the value of the parameter.
     *
     * @param enabled true to enable or false to disable
     */
    protected void setFeedbackButtonsEnabled(boolean enabled) {

        if (confirmButton != null && rescanButton != null) {

            confirmButton.setEnabled(enabled);

            rescanButton.setEnabled(enabled);

        }

    }

    /**
     * setDecoderFormats --
     * Sets what formats the decoder should decode.
     *
     * @param barcodeFormats The barcode formats to decode. If no formats are provided then the
     *                       scanner will default to scanning all bar-code formats.
     * @throws NullPointerException If the array of formats contains a null value
     */
    protected final void setDecoderFormats(@NonNull BarcodeFormat...barcodeFormats) {

        if (Objects.requireNonNull(barcodeFormats).length > 0) {

            List<BarcodeFormat> formatList = new ArrayList<>(barcodeFormats.length);

            Collections.addAll(formatList, barcodeFormats);

            if (formatList.contains(null))

                throw new NullPointerException("Cannot set decode format to a null BarcodeFormat");

            // Apply all the decoder formats
            decBarcodeView.setDecoderFactory(new DefaultDecoderFactory(formatList));

        } else

            decBarcodeView.setDecoderFactory(new DefaultDecoderFactory());

    }

}