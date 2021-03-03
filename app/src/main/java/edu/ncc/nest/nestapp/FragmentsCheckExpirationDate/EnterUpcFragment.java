package edu.ncc.nest.nestapp.FragmentsCheckExpirationDate;
/**
 *
 * Copyright (C) 2020 The LibreFoodPantry Developers.
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
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class EnterUpcFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_upc, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_enter_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Run retrieve UPC to validate input and navigate to correct Fragment
                retrieveUPC(view);

            }
        });

    }

    /**
     * retrieveUPC method - (Taken from EnterUPC class)
     * Gets the UPC from the EditText object the user entered the UPC into
     * and starts the FoodItem activity with the entered UPC
     * @param view
     */

    public void retrieveUPC(View view) {

        // Look in the EditText widget and retrieve the String the user passed in
        EditText editText = (EditText)getView().findViewById(R.id.edittext_enter_upc);
        Log.d("SCAN", "Made it!");
        String upc = editText.getText().toString();

        // Check validity of the UPC
        if(upc.equals("") || upc.length() < 12 || upc.length() >12){
            Toast.makeText(this.getContext(),"UPC length is 12 numbers, please enter a 12-digit number", Toast.LENGTH_SHORT).show();
        } else {
            NestDBDataSource dataSource = new NestDBDataSource(getContext());
            NestUPC result = dataSource.getNestUPC(upc);

            // If there is a result from the database
            if(result != null) {

                // Put the item in a bundle and pass it to ConfirmItemFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("foodItem", result);
                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);
                NavHostFragment.findNavController(EnterUpcFragment.this).navigate((R.id.confirmItemFragment));

                // If there was no result from the database
            }else {

                // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
                Bundle bundle = new Bundle();
                bundle.putString("barcode", upc);
                getParentFragmentManager().setFragmentResult("BARCODE", bundle);
                NavHostFragment.findNavController(EnterUpcFragment.this).navigate((R.id.selectItemFragment));
            }
        }

    }
}