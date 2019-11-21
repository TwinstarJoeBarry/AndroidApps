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

public class Scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

        private ZXingScannerView scannerView;
        private TextView txtResult;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scanner);

            Intent intent = getIntent();

            //init
            scannerView = (ZXingScannerView) findViewById(R.id.zxscan);
            txtResult = (TextView) findViewById(R.id.txt_result);
            //request permission
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            scannerView.setResultHandler(Scanner.this);
                            scannerView.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(Scanner.this, "you must acccept this permission", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    })
                    .check();
        }
        @Override
        protected void onDestroy(){
            scannerView.stopCamera();
            super.onDestroy();
        }

        @Override
        public void handleResult(Result rawResult) {
            //here we can recieve rawResult
            txtResult.setText(rawResult.getText());
            //scannerView.startCamera();
            String str = rawResult.getText().toString();
            Intent intent = new Intent(this, FoodItem.class);
            intent.putExtra("barcode", str);
            startActivity(intent);
            finish();

        }

    }

