package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class UPCScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private TextView txtResult;

    /**
     * Name: onCreate Method
     * displays initial scanner layout. request permission to use the camera.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        txtResult = (TextView) findViewById(R.id.txt_result);
        //request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(UPCScanner.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(UPCScanner.this, "you must accept this permission", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    // This method was causing the app to crash when the back button was being pressed.

//    @Override
//    protected void onDestroy() {
//        scannerView.stopCamera();
//        super.onDestroy();
//    }

    @Override
    public void handleResult(Result rawResult) {
        //here we can recieve rawResult
        txtResult.setText(rawResult.getText());
        // I think the camera should be stopped not started here
        scannerView.stopCamera();
        String str = rawResult.getText();
        Intent intent = new Intent(this, UPCLookup.class);

        intent.putExtra("barcode", str);
        startActivity(intent);
        finish();

    }

}

