package edu.ncc.nest.nestapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class InterfaceTests extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface_tests);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interfaceOneBtn:
                launchInterfaceOne();
                break;
            case R.id.interfaceTwoBtn:
                launchInterfaceTwo();
                break;
            case R.id.interfaceThreeBtn:
                launchInterfaceThree();
                break;
            case R.id.interfaceFourBtn:
                launchInterfaceFour();
                break;
        }
    }

    // launches interface one
    public void launchInterfaceOne() {
        Intent intent = new Intent(this, ItemInformation.class);
        startActivity(intent);
    }

    // launches interface two
    public void launchInterfaceTwo() {

    }

    // launches interface three
    public void launchInterfaceThree() {

    }

    // launches interface four
    public void launchInterfaceFour() {

    }
}
