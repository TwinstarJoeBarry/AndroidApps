package edu.ncc.nest.nestapp.FragmentsUpc;
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

import java.util.List;

import edu.ncc.nest.nestapp.InventoryEntry;
import edu.ncc.nest.nestapp.InventoryInfoSource;
import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

public class DisplayTrueExpirationFragment extends Fragment {
    private NestDBDataSource dataSource;
    private int itemId;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // value of this variable must be updated from the previous fragment
        // hardcoded for testing purposes
        itemId =  605 ;

        // Instantiating
        dataSource = new NestDBDataSource(this.getContext());

        // Getting the shelflife of the product
        List<ShelfLife> item = dataSource.getShelfLivesForProduct(itemId);

        // item is not empty
        if(!item.isEmpty())
            Log.d("TESTING", "onCreateView: " +  item.get(0).toString() );

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_true_expiration, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_display_date_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigation has yet to be set up in the nav_graph.xml
                NavHostFragment.findNavController(DisplayTrueExpirationFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
            }
        });


    }
}