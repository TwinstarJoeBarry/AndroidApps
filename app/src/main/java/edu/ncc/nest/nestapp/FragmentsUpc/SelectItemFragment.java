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
import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestNewUPC;
import edu.ncc.nest.nestapp.R;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;


public class SelectItemFragment extends Fragment implements View.OnClickListener
{
    /** CONSTANT DEFAULTS **/
    // FOR NON ESSENTIAL TEXT ENTRY VIEWS THAT WERE OPTIONAL AND INTENTIONALLY LEFT BLANK;
    private final String DEFAULT_STRING = "VALUE LEFT BLANK";
    // FOR ESSENTIAL TEXT ENTRY VIEWS THAT SHOULD NEVER BE BLANK AND REPLACED IN CODE
    private final String PLACEHOLDER_STRING = "ERROR - VALUE NOT RECEIVED";

    // (DETAILED IN THE INSTANCE VARS SECTION)
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


    /** INSTANCE VARS **/
    private String upcSaved; // UPC as passed from previous fragment; populated from a saved bundle

    int categoryIndex; // index selected for the main category, used to slice the categories array
    private int itemID; // index of sub category, used to parse the product name and id

    // To access UI elements; both directly and indirectly;
    Button categoryButton, productButton;
    TextView categoryHint, productHint;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // initialize vars; placeholder values replaced later
        upcSaved = PLACEHOLDER_STRING;
        categoryIndex = 0;
        itemID = -1;

        return inflater.inflate(R.layout.fragment_select_item, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // INITIALIZE UI ELEMENTS THAT ARE INSTANCE VARIABLES
        categoryHint = view.findViewById(R.id.fragment_select_item_category_hint);
        categoryButton = view.findViewById(R.id.fragment_select_item_category_select);
        categoryButton.setOnClickListener( v -> showCategories() );

        productHint = view.findViewById(R.id.fragment_select_item_product_hint);
        productButton = view.findViewById(R.id.fragment_select_item_product_select);
        productButton.setOnClickListener( v -> showProducts() );

        // GRAB THE UPC VALUES FROM THE BUNDLE SENT FROM SCAN FRAG OR CONFIRM UPC FRAG WHEN READY
        getParentFragmentManager().setFragmentResultListener("BARCODE", this, new FragmentResultListener()
            {
                @Override public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle)
                {
                    upcSaved = bundle.getString("barcode");
                    ((TextView)getView().findViewById(R.id.fragment_select_item_headline)).setText("Adding UPC:" + upcSaved);
                }
            });

        // ACCEPT BUTTON CODE - PARSE VALUES FOR NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.acceptButton).setOnClickListener( view1 ->
        {
            // open a source to the database to add the information;
            NestDBDataSource database = new NestDBDataSource( getContext() );

            // retrieve the String information from each view, casting as appropriate;
            String name = ((EditText) (getView().findViewById(R.id.fragment_select_item_brand_entry))).getText().toString();
            String description = ((EditText) (getView().findViewById(R.id.fragment_select_item_description_entry))).getText().toString();


            // replace any fields from above with blank values;
            if ( name.isEmpty() )
                name = DEFAULT_STRING;
            if ( description.isEmpty() )
                description = DEFAULT_STRING;

            // and assert that the values that CANNOT be blank are not.
            if (itemID == -1)
                Toast.makeText(getContext(), "Don't forget to fill the category AND subcategory!", Toast.LENGTH_LONG).show();

            else
            {
                // use our source to the database to add the new upc to the NEST's table
                database.insertNewUPC(upcSaved, name, description, itemID);

                // stuff everything in a bundle in case it's needed for PrintedExpirationDate;
                Bundle saved = new Bundle();
                saved.putInt("savedID", itemID);
                saved.putString("savedUPC", upcSaved);
                saved.putString("savedProductName", name);
                saved.putString("savedDescription", description);
                getParentFragmentManager().setFragmentResult("PRODUCT INFO", saved);

                // navigate over to printed expiration date;
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.action_selectItemFragment_to_selectPrintedExpirationDateFragment);
            }
        });


        // CANCEL BUTTON CODE - NAVIGATE BACK TO ENTER UPC FRAG
        view.findViewById(R.id.cancelButton).setOnClickListener( view12 ->
            NavHostFragment.findNavController(SelectItemFragment.this).navigate(R.id.confirmItemFragment));

    }


    /**
     * showCategories()
     * MAIN CATEGORY POPUP MENU FOR SELECT CATEGORY BUTTON
     **/
    private void showCategories()
    {
        // SHOW THE POP UP MENU FOR THE MAIN CATEGORIES, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), categoryButton);
        Menu menu = menuPop.getMenu();

        String[] mainCategories = getResources().getStringArray(R.array.categories_array);

        for (int i = 0; i < mainCategories.length; ++i )
            menu.add(1, i, 1, mainCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item ->
        {
            categoryHint.setText( item.toString() );
            categoryIndex = item.getItemId();
            productHint.setText(" ");
            itemID =  -1;
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
        // SHOW THE POP UP MENU FOR THE SUB CATEGORIES, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), productButton);
        Menu menu = menuPop.getMenu();

        String[] subCategories = getResources().getStringArray( categories[categoryIndex] );

        for (int i = 0; i < subCategories.length; ++i )
            menu.add(categoryIndex, i, i, subCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE PRODUCT ID AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item ->
        {
            productHint.setText( item.toString() );
            itemID = item.getItemId();
            return true;
        });

        menuPop.show();
    }

    /** UNUSED METHODS - NEEDED FOR INTERFACES THAT ARE IMPLEMENTED **/
    @Override public void onClick(View v) {}


}