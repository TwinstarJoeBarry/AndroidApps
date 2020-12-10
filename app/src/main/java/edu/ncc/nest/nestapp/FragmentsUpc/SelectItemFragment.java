package edu.ncc.nest.nestapp.FragmentsUpc;
/*
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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.NestDBDataSource;


public class SelectItemFragment extends Fragment implements View.OnClickListener
{
    private String upcSaved; // UPC as passed from previous fragment; populated from a saved bundle

    // THE INDEX SELECTED FROM A CATEGORY POPUP, POPULATES A SUB CATEGORY (ITEM) POPUP, SETS itemID
    private int itemID; // the product identification number, as selected from popup menus
    int categoryIndex; // index selected for the main category, used to slice the following array

    // TODO list would be better populated programmatically, instead of hardcoded like this
    private final int categories[] =
    {
        R.array.baby_food_items,
        R.array.baked_goods_bakery_items,
        R.array.beverages_items,
        R.array.condiments_sauces_and_canned_goods_items,
        R.array.dairy_products_and_eggs_items,
        R.array.deli_and_prepared_foods_items,
        R.array.food_purchased_frozen_items,
        R.array.grains_beans_and_pasta_items,
        R.array.meat_fresh_items,
        R.array.poultry_fresh_items,
        R.array.produce_fresh_fruits_items,
        R.array.seafood_fresh_items,
        R.array.shelf_stable_foods_items,
        R.array.vegetarian_protein_items,
    };

    // UI BUTTONS FOR THE CATEGORY AND SUB CATEGORY
    Button categoryButton, productButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // initialized values;
        upcSaved = "";
        itemID = categoryIndex = 0;

        return inflater.inflate(R.layout.fragment_select_item, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);


        // GRAB THE UPC VALUES FROM THE BUNDLE SENT FROM SCAN FRAG OR CONFIRM UPC FRAG
        getParentFragmentManager().setFragmentResultListener("BARCODE", this,
                (requestKey, result) -> upcSaved = result.getString("barcode"));

        Toast.makeText(getContext(), String.format("GOT UPC: %s", upcSaved), Toast.LENGTH_LONG).show(); // TODO


        // ON CLICKS FOR MAIN CATEGORY AND SUB CATEGORY BUTTONS - POPULATES A MENU VIEW FOR EACH
        categoryButton = view.findViewById(R.id.category_select);
        categoryButton.setOnClickListener( v -> showCategories() );

        productButton = view.findViewById(R.id.item_select);
        productButton.setOnClickListener( v -> showProducts() );


        // ACCEPT BUTTON CODE - PARSE AND ADD VALES TO NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.acceptButton).setOnClickListener( view1 ->
        {
            // open a link to the database to add the information;
            NestDBDataSource database = new NestDBDataSource(getContext());

            // retrieve the String information from each view, casting as appropriate;
            String name = ((EditText) (getView().findViewById(R.id.brandEdit))).getText().toString();
            String description = ((EditText) (getView().findViewById(R.id.descriptionEdit))).getText().toString();


            // make sure all fields were was available and entered properly, including an item id
            if (name.isEmpty() || description.isEmpty() || itemID == 0 )
//                Toast.makeText(getContext(), "Please fill and select all fields!, including all sub categories", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), String.format("REJECTED! name: %s des: %s id: %d", name, description, itemID), Toast.LENGTH_LONG).show();
            else
            {
                // use dataSource to add the new upc to the Nest UPCs table
                Toast.makeText(getContext(), String.format("ACCEPTED! name: %s des: %s id: %d", name, description, itemID), Toast.LENGTH_LONG).show();
//                database.insertNewUPC(upcSaved, name, description, itemID);


                // stuff everything in a bundle in case it's needed for PrintedExpirationDate
                Bundle saved = new Bundle();
                saved.putInt("savedID", itemID);
                saved.putString("savedUPC", upcSaved);
                saved.putString("savedProductName", name);
                saved.putString("savedDescription", description);
                getParentFragmentManager().setFragmentResult("PRODUCT INFO", saved);

                // next stop -> printed expiration date
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.action_selectItemFragment_to_selectPrintedExpirationDateFragment);
            }
        });


        // CANCEL BUTTON CODE - NAVIGATE BACK TO ENTER UPC FRAG
        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.confirmItemFragment);
            }
        });


    }


    /**
     * showCategories()
     * MAIN CATEGORY POPUP MENU FOR SELECT CATEGORY BUTTON
     **/
    private void showCategories()
    {
        // SHOW THE POP UP MENU FOR THE MAIN CATEGORIES
        PopupMenu menuPop = new PopupMenu(getContext(), categoryButton);
        Menu menu = menuPop.getMenu();

        String[] mainCategories = getResources().getStringArray(R.array.categories_array);

        for (int i = 0; i < mainCategories.length; ++i )
            menu.add(i, i, i, mainCategories[i]);

//        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX
        menuPop.setOnMenuItemClickListener(item ->
        {
            categoryIndex = item.getItemId();
            return true;
        });

        menuPop.show();
    }


    /**
     * showProduct()
     * SUB CATEGORY POP UP MENU
     **/
    private void showProducts()
    {
        // SHOW THE POP UP MENU FOR THE SUB CATEGORIES
        PopupMenu menuPop = new PopupMenu(getContext(), productButton);
        Menu menu = menuPop.getMenu();

        String[] subCategories = getResources().getStringArray( categories[categoryIndex] );

        for (int i = 0; i < subCategories.length; ++i )
            menu.add(i, i, i, subCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE ITEM ID NUMBER
        menuPop.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item ->
        {
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });

        menuPop.show();
    }

    /** UNUSED METHODS - NEEDED FOR INTERFACES THAT ARE IMPLEMENTED **/
    @Override public void onClick(View v) {}


}