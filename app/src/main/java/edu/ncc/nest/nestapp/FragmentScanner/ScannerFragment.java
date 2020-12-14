package edu.ncc.nest.nestapp.FragmentScanner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

public abstract class ScannerFragment extends Fragment implements BarcodeCallback, View.OnClickListener {

    public static final String TAG = ScannerFragment.class.getSimpleName();

    private static final List<BarcodeFormat> BARCODE_FORMATS = Collections.singletonList(BarcodeFormat.CODE_39);
    // To support multiple formats change this to Arrays.asList() and fill it with the required
    // formats. For example, Arrays.asList(BarcodeFormat.CODE_39, BarcodeFormat.UPC_A, ...);

    // Used to ask for camera permission. Calls onCameraPermissionResult method with the result
    private final ActivityResultLauncher<String> REQUEST_CAMERA_PERMISSION_LAUNCHER = registerForActivityResult(
            new RequestPermission(), this::onCameraPermissionResult);

    private static final long SCAN_DELAY = 1500L;   // 1.5 Seconds in milliseconds

    private DecoratedBarcodeView decBarcodeView;
    private BeepManager beepManager;
    private TextView resultTextView;
    private Button confirmButton;
    private Button rescanButton;

    private boolean scannerPaused = true;

    // Stores the bar code that has been scanned
    private String barcodeResult = null;

    protected boolean debug = true;


    ////////////// Abstract Methods Start //////////////

    protected abstract void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format);


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
        decBarcodeView = view.findViewById(R.id.zxing_barcode_scanner);

        resultTextView = view.findViewById(R.id.guest_scan_result_view);

        confirmButton = view.findViewById(R.id.guest_scan_confirm_button);

        rescanButton = view.findViewById(R.id.guest_scan_rescan_button);


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
                != PackageManager.PERMISSION_GRANTED) {

            // Update the status text to inform the guest that camera permission is required
            decBarcodeView.setStatusText(getString(R.string.guest_scan_camera_permission_required));

            // Clear the result text view
            resultTextView.setText(null);

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                // TODO Create a dialog window describing why we need the permission for this feature

                // Display a reason of why we need the permission
                Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                        Toast.LENGTH_LONG).show();

            } else

                // Request the camera permission
                REQUEST_CAMERA_PERMISSION_LAUNCHER.launch(Manifest.permission.CAMERA);

        } else

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
        if (decBarcodeView != null && !scannerPaused) {

            // Since we are destroying the fragment, pause and wait for the camera to close
            decBarcodeView.pauseAndWait();

            scannerPaused = true;

        }

        super.onDestroy();

    }


    ////////////// Other Event Methods Start  //////////////

    @Override
    public final void barcodeResult(BarcodeResult result) {

        // Gets the barcode from the result
        String resultText = result.getText();

        // Make sure we actually have a barcode scanned
        if (resultText != null) {

            // Play a sound and vibrate when a scan has been processed
            beepManager.playBeepSoundAndVibrate();

            // Tell the user to confirm that the barcode is correct
            decBarcodeView.setStatusText(getString(R.string.guest_scan_confirm_msg));

            // Display the barcode back to the user
            resultTextView.setText(resultText);

            // Store the barcode
            barcodeResult = resultText;

            // Pause the scanner
            decBarcodeView.pause();

            scannerPaused = true;

            // Enable the feedback buttons after we have stored the bar-code and stopped scanner
            setFeedbackButtonsEnabled(true);

            if (debug)

                Log.d(TAG, "Barcode Result: " + resultText + ", Barcode Format: " + result.getBarcodeFormat());

        } else

            // Scan for another bar-code
            decBarcodeView.decodeSingle(ScannerFragment.this);

    }

    @Override
    public void onClick(View view) {

        // NOTE: Removed permission check here since buttons will be disabled until a scan is performed

        int id = view.getId();

        if (id == R.id.guest_scan_rescan_button)

            resumeScanning();

        else if (id == R.id.guest_scan_confirm_button && barcodeResult != null) {

            if (debug)

                Log.d(TAG, "Scan Confirmed: " + barcodeResult);

            // Create the Bundle that will be used to send the barcode to the next fragment
            Bundle resultBundle = new Bundle();

            // Put the barcodeResult into the bundle
            resultBundle.putString("BARCODE", barcodeResult);

            // Create an instance of the database helper
            GuestFormHelper db = new GuestFormHelper(requireContext());

            // Check if the guest is registered in the database
            // No guests yet so this will always be null. GUEST_NAME was set to "Test" for testing purposes.
            //final String GUEST_NAME = db.isRegistered(barcodeResult);
            //TODO: Replace the line below with the one above.
            final String GUEST_NAME = "Test";

            if (GUEST_NAME != null) {

                // If the guest is registered, include the guest's name in the result
                resultBundle.putString("GUEST_NAME", GUEST_NAME);

            }

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("CONFIRM_SCAN", resultBundle);

            // Navigate to the confirmation fragment
            /*NavHostFragment.findNavController(ScannerFragment.this)
                    .navigate(R.id.action_GuestScanFragment_to_GuestScanConfirmationFragment);*/

        }

    }


    ////////////// Custom Methods Start  //////////////

    /**
     * Takes 1 parameter. This method gets called by the REQUEST_CAMERA_PERMISSION_LAUNCHER, after
     * asking for camera permission. Determines what happens when the permission gets granted or
     * denied.
     * @param isGranted - true if permission was granted false otherwise
     */
    private void onCameraPermissionResult(boolean isGranted) {

        if (!isGranted) {

            // Display a reason of why we need the permission
            Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                    Toast.LENGTH_LONG).show();

        } else

            // Camera permission is granted, so resume scanning
            resumeScanning();

    }

    /**
     * Takes 0 parameters. Resumes the scanner if it is not paused, resets resultTextView text,
     * resets the barcodeResult to be null so we can scan a new bar-code, and starts the decoder
     * after a delay of SCAN_DELAY.
     */
    private void resumeScanning() {

        if (scannerPaused) {

            // Update the status text to explain how to use the scanner
            decBarcodeView.setStatusText(getString(R.string.zxing_msg_default_status));

            // Update the display text so the user knows we are waiting for them to scan a barcode
            resultTextView.setText(getString(R.string.guest_scan_waiting_for_scan));

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

                // As long as the scanner hasn't been paused, start the decoder
                if (!scannerPaused)

                    // Tells the decoder to stop after a single scan
                    decBarcodeView.decodeSingle(ScannerFragment.this);

            }, SCAN_DELAY);

        }

    }

    /**
     * Takes 1 parameter. Toggles whether both rescanButton and confirmScan button are enabled or
     * disabled, based on the value of the parameter.
     *
     * @param enabled true to enable or false to disable
     */
    private void setFeedbackButtonsEnabled(boolean enabled) {

        confirmButton.setEnabled(enabled);

        rescanButton.setEnabled(enabled);

    }

}
