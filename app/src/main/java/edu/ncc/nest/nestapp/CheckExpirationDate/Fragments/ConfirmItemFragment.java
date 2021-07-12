package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2020 The LibreFoodPantry Developers.
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * ConfirmItemFragment: Ask the user to confirm that the item that pulled from the database, is the
 * correct item they wanted.
 *
 * Navigates to {@link SelectPrintedExpirationDateFragment} with the item, if the user confirms the
 * item is correct.
 *
 * Navigates to {@link SelectItemFragment} with the UPC barcode, if the item is incorrect.
 */
public class ConfirmItemFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    private static final String TAG = ConfirmItemFragment.class.getSimpleName();

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_confirm_item,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve fragment result
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            if (result.containsKey("upcBarcode")) {

                NestDBDataSource dataSource = CheckExpirationDateActivity
                        .requireDataSource(ConfirmItemFragment.this);

                foodItem = dataSource.getNestUPC(result.getString("upcBarcode"));

            } else

                foodItem = (NestUPC) result.getSerializable("foodItem");

            assert foodItem != null : "Failed to retrieve required data";

            // Display item information
            ((TextView) view.findViewById(R.id.item)).setText(foodItem.getProductName());

            ((TextView) view.findViewById(R.id.upc)).setText(foodItem.getUpc());

            ((TextView) view.findViewById(R.id.category)).setText(foodItem.getCatDesc());

            ((TextView) view.findViewById(R.id.type)).setText(foodItem.getProductSubtitle());

            ((TextView) view.findViewById(R.id.brand)).setText(foodItem.getBrand());

            ((TextView) view.findViewById(R.id.description)).setText(foodItem.getDescription());

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        /* If the "Confirm" button was clicked, navigate to
         * fragment_check_expiration_date_select_printed_expiration_date.xml */
        view.findViewById(R.id.confirm_item_btn).setOnClickListener(view1 -> {

            assert foodItem != null : "foodItem is null";

            Bundle result = new Bundle();

            result.putSerializable("foodItem", foodItem);

            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            NavHostFragment.findNavController(ConfirmItemFragment.this)
                    .navigate(R.id.action_CED_ConfirmItemFragment_to_SelectPrintedExpirationDateFragment);

        });

        /* If the "Incorrect" button was clicked, navigate to
         * fragment_check_expiration_date_select_item.xml */
        view.findViewById(R.id.incorrect_btn).setOnClickListener(view12 -> {

            assert foodItem != null : "foodItem is null";

            // Send UPC/barcode to SelectItemFragment
            Bundle result = new Bundle();

            // Using "barcode" KEY to stay consistent with ScannerFragment
            result.putString("upcBarcode", foodItem.getUpc());

            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            NavHostFragment.findNavController(ConfirmItemFragment.this)
                    .navigate(R.id.action_CED_ConfirmItemFragment_to_SelectItemFragment);

        });

    }

}
