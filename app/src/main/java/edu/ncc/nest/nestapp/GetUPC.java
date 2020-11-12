package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class GetUPC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_upc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * launchScanner - starts the Scanner activity
     */
    public void launchScanner(View view) {
        Intent intent = new Intent(this, Scanner.class);
        startActivity(intent);
    }

    /**
     * launchEnterUPC - starts the Enter UPC activity
     */
    public void launchEnterUPC(View view) {
        Intent intent = new Intent(this, EnterUPC.class);
        startActivity(intent);
    }

    /**
     * onCreateOptionsMenu method --
     * Creates the objects in the app bar
     * @param menu - a default menu object
     * @return true if method runs correctly
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * onOptionsItemSelected method --
     * Determines what happens based on which item in the app bar was selected
     * @param item - the item that was selected
     * @return super.onOptionsItemSelected(item), a command to start the method again
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method - goes to the home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

}
