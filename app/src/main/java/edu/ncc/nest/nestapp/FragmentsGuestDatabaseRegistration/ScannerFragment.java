package edu.ncc.nest.nestapp.FragmentsGuestDatabaseRegistration;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.FragmentScanner.AbstractScannerFragment;

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
