package edu.ncc.nest.nestapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        // ItemInformation.java is deprecated
        //Intent intent = new Intent(this, ItemInformation.class);
        //startActivity(intent);
    }

    // launches interface two
    public void launchInterfaceTwo() {
        // Scanner.java is deprecated
        //Intent intent = new Intent(this, Scanner.class);
        //startActivity(intent);
    }

    // launches interface three
    public void launchInterfaceThree() {
        Intent intent = new Intent(this, Donate.class);
        startActivity(intent);
    }

    // launches interface four
    public void launchInterfaceFour() {
        Intent intent = new Intent(this, UPCLookup.class);
        startActivity(intent);
    }
}
