package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity hopefully to be used as an intent for addToInventory
 */
public class ATIQuestionaire extends AppCompatActivity implements View.OnClickListener {
    Button btn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_t_i_questionaire);

        //gets intent from addToInventory activity
        Intent intent = getIntent();
        TextView categoryTitle = findViewById(R.id.categoryChosen);
        categoryTitle.setText(intent.getStringExtra("categoryLabel"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * implements the menu options for the toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * MenuItem selection
     * @param item Whatever is clicked on the menu item
     * @return Will return to the nest home once the
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeBtn) {
            home();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method - goes to the nest home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

    }
}
