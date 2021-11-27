package edu.ncc.nest.nestapp;
/**
 *
 * Copyright (C) 2019 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import android.app.ListActivity;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class LocalDatabase extends ListActivity {

    private static final String TAG = "TESTING";
    private InventoryInfoSource datasource;
    private ArrayAdapter<InventoryEntry> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_database);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // not supported in a ListActivity
        //setSupportActionBar(toolbar);

        datasource = new InventoryInfoSource(this);
        datasource.open();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

        List<InventoryEntry> items;

        switch (view.getId()) {
            case R.id.show:
                items = datasource.getProducts();
                adapter = new ArrayAdapter<InventoryEntry>(this, android.R.layout.simple_list_item_1, items);
                setListAdapter(adapter);
                break;
            case R.id.delete:
                // method only to be used by developer to adjust the local database
                //datasource.removeRecord();
                break;

        }
        adapter.notifyDataSetChanged();
    }

    public void onDestroy()
    {
        datasource.close();
        super.onDestroy();
    }

}
