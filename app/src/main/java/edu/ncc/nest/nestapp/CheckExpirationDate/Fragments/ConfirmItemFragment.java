package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;
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
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

public class ConfirmItemFragment extends Fragment {

    private static final String TAG_ITEM = "TEST ITEM";
    private static final String TAG = "TESTING";

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

        Log.d(TAG, "In ConfirmItemFragment onCreateView()");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_confirm_item, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "In ConfirmItemFragment onViewCreated()");

        // Get respective views from layout
        itemName = (TextView) view.findViewById(R.id.add_item_name);
        catName = (TextView) view.findViewById(R.id.add_cat_name);
        upc = (TextView) view.findViewById(R.id.add_UPC);

        if (savedInstanceState != null) {

            Log.d(TAG, "In ConfirmItemFragment onViewCreated() If savedInstanceSate != null");

            // get Strings, assign to TextViews
            // retrieve foodItem from Bundle
            item = (NestUPC) savedInstanceState.get("foodItem");

            // retrieve strings from food item
            item_name = item.getProductName();
            Log.d(TAG_ITEM, "Item Name: " + item_name);
            cat_name = item.getCatDesc();
            Log.d(TAG_ITEM, "Cat Desc: " + cat_name);
            upc_string = item.getUpc();
            Log.d(TAG_ITEM, "UPC: " + upc_string);

            // Send retrieved data to TextView
            itemName.setText(item_name);
            catName.setText(cat_name);
            upc.setText(upc_string);
        }

        else {

            // Retrieve Bundle
            getParentFragmentManager().setFragmentResultListener("FOOD ITEM", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    Log.d(TAG, "In ConfirmItemFragment onFragmentResult()");

                    // retrieve foodItem from Bundle
                    item = (NestUPC) result.getSerializable("foodItem");

                    // retrieve strings from food item
                    item_name = item.getProductName();
                    Log.d(TAG_ITEM, "Item Name: " + item_name);
                    cat_name = item.getCatDesc();
                    Log.d(TAG_ITEM, "Cat Desc: " + cat_name);
                    upc_string = item.getUpc();
                    Log.d(TAG_ITEM, "UPC: " + upc_string);

                    // Send retrieved data to TextView
                    itemName.setText(item_name);
                    catName.setText(cat_name);
                    upc.setText(upc_string);

                    // Clear the result listener since we successfully received the result
                    getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

                }
            });
        }



        ////////////// Navigation //////////////

        // if confirm button clicked, nav to fragment_check_expiration_select_printed_expiration_date.xml
        view.findViewById(R.id.button_confirm_item).setOnClickListener(view1 -> {

            Log.d(TAG, "In ConfirmItemFragment onClick() for button_confirm_item");

            // Send bundle received to SelectPrintedExpirationDateFragment
            Bundle bundle = new Bundle();

            bundle.putSerializable("foodItem", item);

            // Need to clear the result with the same request key, before using possibly same request key again.
            getParentFragmentManager().clearFragmentResult("FOOD ITEM");

            getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

            NavHostFragment.findNavController(ConfirmItemFragment.this)
                    .navigate(R.id.action_CE_ConfirmItemFragment_to_SelectPrintedExpirationDateFragment);

        });

        // if the item was incorrect, nav to fragment_check_expiration_select_item.xml
        view.findViewById(R.id.button_incorrect_item).setOnClickListener(view12 -> {

            Log.d(TAG, "In ConfirmItemFragment onClick() for button_incorrect_item");

            // Send UPC/barcode to SelectItemFragment
            Bundle upcBundle = new Bundle();

            upcBundle.putString("barcode", upc_string);     // using "barcode" KEY to stay consistent with AbstractScannerFragment

            // Need to clear the result with the same request key, before using possibly same request key again.
            getParentFragmentManager().clearFragmentResult("BARCODE");

            getParentFragmentManager().setFragmentResult("BARCODE", upcBundle);

            NavHostFragment.findNavController(ConfirmItemFragment.this)
                    .navigate(R.id.action_CE_ConfirmItemFragment_to_SelectItemFragment);


        });
    }

    /////////// LIFE CYCLE ////////////
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (item != null)

            // save item + strings
            outState.putSerializable("foodItem", item);

        super.onSaveInstanceState(outState);

    }


}