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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class ConfirmItemFragment extends Fragment {

    private Button confirmButton;
    private Button incorrectButton;
    private TextView itemName;
    private TextView catName;
    private TextView upc;

    private String item_name;
    private String cat_name;
    private String upc_string;

    private NestUPC item;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_item, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            // Get respective views from layout
            confirmButton = (Button) view.findViewById(R.id.button_confirm_item);
            incorrectButton = (Button) view.findViewById(R.id.button_incorrect_item);
            itemName = (TextView) view.findViewById(R.id.add_item_name);
            catName = (TextView) view.findViewById(R.id.add_cat_name);
            upc = (TextView) view.findViewById(R.id.add_UPC);

            // Retrieve Strings from data
            Bundle bundle = new Bundle();
            item = (NestUPC) bundle.get("GET FOOD ITEM");
            item_name = item.getProductName();
            cat_name = item.getCatDesc();
            upc_string = item.getUpc();

            // Sed retrieved data to TextView
            itemName.setText(item_name);
            catName.setText(cat_name);
            upc.setText(upc_string);

            ////////////// Navigation //////////////

            // if confirm button clicked, nav to fragment_select_printed_expiration_date.xml
            view.findViewById(R.id.button_confirm_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NavHostFragment.findNavController(ConfirmItemFragment.this)
                            .navigate(R.id.action_confirmItemFragment_to_selectPrintedExpirationDateFragment);

                }
            });

            // if the item was incorrect, nav to fragment_select_item.xml
            view.findViewById(R.id.button_incorrect_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NavHostFragment.findNavController(ConfirmItemFragment.this)
                            .navigate(R.id.action_confirmItemFragment_to_selectItemFragment);

                }
            });

    }
}