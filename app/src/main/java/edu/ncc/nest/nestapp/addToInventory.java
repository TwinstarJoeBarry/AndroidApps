package edu.ncc.nest.nestapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

public class addToInventory extends AppCompatActivity {
    private GridView gridView;
    public static int categoryImages[] = {R.drawable.baby_food, R.drawable.baked_goods,R.drawable.beverages, R.drawable.condiments_sauce_and_can_goods};
    public static String categoryName[] = {"Baby Food", "Baked Goods", "Beverages", "Condiments, Sauces, and Canned Goods"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_inventory);



        gridView = (GridView) findViewById(R.id.gridView);
        CategoryAdapter adapter = new CategoryAdapter(this );
        gridView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }


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
