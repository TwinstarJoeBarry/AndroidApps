package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "**Login**";
    EditText emailLogin, passwordLogin;
    ImageView btnSubmit;
    CardView btnReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        emailLogin = findViewById(R.id.login_email);
        passwordLogin = findViewById(R.id.login_password);
        btnSubmit = findViewById(R.id.submit_btn);
        //btnReg = findViewById(R.id.btnReg);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = emailLogin.getText().toString();
                String strPassword = passwordLogin.getText().toString();
                //String strEmailPass = strEmail + ", " + srtPassword;

                Log.d(TAG, "User email: " + strEmail + " and password: "
                        + strPassword);

                //Toast.makeText(LoginActivity.this, " " + strEmailPass,
                        //Toast.LENGTH_SHORT).show();
            }
        });
    }

    }