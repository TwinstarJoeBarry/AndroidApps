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
import edu.ncc.nest.nestapp.R;

public class SelectItemFragment extends Fragment {
    private InventoryInfoSource datasource;
    private List<InventoryEntry> items;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        datasource = new InventoryInfoSource(this.getContext());
        datasource.open();

        items = datasource.getProducts();



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_item, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_select_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigation has yet to be set up in the nav_graph.xml
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.action_StartFragment_to_ScanFragment);
            }
        });

        view.findViewById(R.id.showInventory).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                Log.d("TEST", "It got here");
//                items = datasource.getProducts();
                if(items.isEmpty()) {
                    Log.d("TEST", "It's empty");
                }
                Log.d("TEST", "It got here");
                for(int i = 0; i < items.size(); i++){
                    Log.d("TEST", items.get(i).getBarcodeNum());
                }

            }
        });
    }




}