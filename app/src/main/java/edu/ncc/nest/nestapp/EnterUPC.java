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

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @deprecated This Activity is being replaced by a Fragment. ({@link edu.ncc.nest.nestapp.FragmentsCheckExpiration.EnterUpcFragment})
 */
@Deprecated
public class EnterUPC extends AppCompatActivity {

    @Override
    /**
     * onCreate method -
     * loads the 'activity_enter_upc layout' and creates the toolbar
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_upc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * retrieveUPC method -
     * Gets the UPC from the EditText object the user entered the UPC into
     * and starts the FoodItem activity with the entered UPC
     * @param view
     */

    public void retrieveUPC(View view) {
        EditText editText = findViewById(R.id.enter_UPC_editText);
        String upc = editText.getText().toString();

        Intent intent = new Intent(this, FoodItem.class);

        if(upc.equals("") || upc.length() < 12 || upc.length() >12){
            Toast.makeText(getApplicationContext(),"UPC length is 12 numbers, please enter larger number", Toast.LENGTH_SHORT).show();
        }
        else{
            intent.putExtra("barcode", upc);
            startActivity(intent);
            finish();
        }
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


    /**
     * processUPC method --
     * onClick for the Lookup button, processes the UPC code that was
     * entered or scanned-in as follows:
     * - lookup the UPC code in the NestUPCs table (might be in separate database at some point)
     * - if found, fill out all the remaining fields based on the associated foodkeeper product,
     *   including doing the calculating of the "real" expiration date(s) (would be good to
     *   do entry/select of "package" expiration date prior to UPC scan/enter)
     *  - if not found, start the NestNewUPC activity so the Nest volunteer can add it
     *   as a new upc in the database.  If that activity is successful it should return with
     *   the newly associated FoodKeeper product id.  Proceed then as if the UPC had been found
     *   to begin with.
     * @param view - the processUPC button
     */
    /*
    public void lookupUPC(View view){
        EditText editText = findViewById(R.id.enter_UPC_editText);
        String upc = editText.getText().toString();

        if(upc.equals("") || upc.length() < 12 || upc.length() >12){
            Toast.makeText(getApplicationContext(),"UPC length is 12 numbers, please enter larger number", Toast.LENGTH_SHORT).show();
        }
        else {
            int productId = dataSource.getProductIdFromUPC(upc); // returns -1 if not found
            if (productId < 0) {
                // upc not found in Nest UPCs table, let's add it...
                Intent intent = new Intent(this, NestNewUPC.class);
                intent.putExtra("upc", upc);
                startActivityForResult(intent, 0);
            } else {
                processDataFromUPC();
            }
        }
    }

     */
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // handle result from NestNewUPC activity launched in processUPC method
        if (resultCode == RESULT_OK) {
            processDataFromUPC();
        }
    }
*/
}