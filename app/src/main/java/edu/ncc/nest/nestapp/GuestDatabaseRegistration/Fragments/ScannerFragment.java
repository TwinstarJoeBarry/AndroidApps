package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

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
