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
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//******************************testing**************************************************************************
import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.Fragments.MoreInfoFragment;
import edu.ncc.nest.nestapp.databinding.ActivityChooseBinding;

public class Choose extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "testing";
    private ActivityChooseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar.getRoot());

        /*setContentView(R.layout.activity_choose);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        //instead of inflating menu_main, now this class inflates choose_menu_main.xml, this way
        // allowing the future efforts button to only appear at the app bar of the launch UI
        inflater.inflate(R.menu.choose_menu_main, menu);

        return true;

        //since the toolbar has 2 different button that goes to different places
        //ask ho to make the button just show up in the launch UI

    }

    /**
     *
     * Title : onClick Method -- Whenever a certain button is clicked it would
     * call the method and inside that method would launch an activity and display to the user
     * and then would break afterwords..
     *
     * @param v - The activity that was clicked by the user.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getUPCBtn:
                launchGetUPC();
                break;
            case R.id.guestFormBtn:
                launchGuestForm();
                break;
        }
    }

    /**
     * launchGuestForm - starts the GuestForm activity
     */
    public void launchGuestForm() {
        Intent intent = new Intent(this, GuestFormTesting.class);
        startActivity(intent);
    }

    /**
     * launchGetUPC - starts the Get UPC activity
     */
    public void launchGetUPC() {
        Intent intent = new Intent(this, CheckExpirationDateActivity.class);
        startActivity(intent);
    }

    /**
     * onOptionsItemSelected method --
     *
     * description: this method handle the action bar items that were clicked.
     * When you click the future efforts button it launches the FutureEfforts class.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.futureEffortBtn) {
            launchFutureEfforts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * launchFutureEfforts - starts the Future Efforts activity
     */
    public void launchFutureEfforts() {
        Intent intent = new Intent(this, FutureEfforts.class);
        startActivity(intent);
    }


    //    ******************************************TESTING
    /**
     * launchTrueDate - starts the MoreInfoFragment fragment
     */
    public void launchTrueDate() {
        ((Button)findViewById(R.id.getUPCBtn)).setVisibility(View.GONE);
        ((Button)findViewById(R.id.guestFormBtn)).setVisibility(View.GONE);
        //((Button)findViewById(R.id.futureEffortsBtn)).setVisibility(View.GONE);
        //((Button)findViewById(R.id.trueDate)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.nestTxt)).setVisibility(View.GONE);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.RelativeLayoutMain, new MoreInfoFragment() ).commit();

    }



}






