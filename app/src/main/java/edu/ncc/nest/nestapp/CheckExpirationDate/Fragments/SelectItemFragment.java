package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;
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
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

// FIXME/TODO: highlights any important notes within the code, highlighted in yellow within android studio
public class SelectItemFragment extends Fragment implements View.OnClickListener
{
    /** CONSTANT DEFAULTS **/
    // FOR NON ESSENTIAL TEXT ENTRY VIEWS THAT WERE OPTIONAL AND INTENTIONALLY LEFT BLANK;
    private final String DEFAULT_STRING = "[LEFT BLANK]";
    // FOR ESSENTIAL TEXT ENTRY VIEWS THAT SHOULD NEVER BE BLANK AND REPLACED IN CODE
    private final String PLACEHOLDER_STRING = "[NOT RECEIVED]";

    // (DETAILED IN THE INSTANCE VARS SECTION)
    private final int categories[] =
    {
            // TODO some present subcategories were not included in R.array.categories in the strings.xml file;
            //  (as of 12.15.2020 all that were currently listed and was able to has been ported)
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
    private String upcSaved; // UPC as passed from previous fragments (populated with saved bundle)

    int categoryIndex; // index selected for the main category, used to slice the categories array
    private int subCategory; // index within the sub category, used to parse a product type and specific id

    // To access UI elements; both directly and indirectly
    Button categoryButton, productButton;
    TextView categoryHint, productHint;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // initialize vars; placeholder values replaced later
        upcSaved = PLACEHOLDER_STRING;
        categoryIndex = -1;
        subCategory = -1;

        return inflater.inflate(R.layout.fragment_check_expiration_select_item, container, false);
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
        getParentFragmentManager().setFragmentResultListener("BARCODE", this, (key, bundle) ->
        {

            upcSaved = bundle.getString("barcode");

            if (!PLACEHOLDER_STRING.equals(upcSaved)) {

                String text = "Adding UPC: " + upcSaved;

                ((TextView) view.findViewById(R.id.fragment_select_item_headline)).setText(text);

            }

            // FIXME nothing to get when navigating backward from next fragment (select printed expiration date); not likely desirable behavior!

            // Clear the result listener since we have successfully retrieved the result
            getParentFragmentManager().clearFragmentResultListener("BARCODE");

        });

        // ACCEPT BUTTON CODE - PARSE VALUES FOR NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.acceptButton).setOnClickListener( view1 ->
        {
            // open a source to the database to add the information;
            NestDBDataSource database = new NestDBDataSource( requireContext() );

            // retrieve the String information from each view, casting as appropriate;
            String name = ((EditText) (view.findViewById(R.id.fragment_select_item_brand_entry))).getText().toString();
            String description = ((EditText) (view.findViewById(R.id.fragment_select_item_description_entry))).getText().toString();


            // replace any fields from above with blank values;
            if ( name.isEmpty() )
                name = DEFAULT_STRING;
            if ( description.isEmpty() )
                description = DEFAULT_STRING;

            // and first assert that the values that CANNOT be blank, are not;
            if (subCategory == -1)
                Toast.makeText(getContext(), "Don't forget to fill the category AND subcategory!", Toast.LENGTH_LONG).show();

            else {

                // use our source to the database to add the new upc to the NEST's table after parsing the ID
                // NOTE: This will return -1 if the UPC has never been added before and will cause errors in future fragments.
                int itemId = database.getProductIdFromUPC(upcSaved);

                if (itemId == -1) {

                    // Product ID does not exist for this UPC
                    // TODO: Need a way of setting the proper productId

                    Log.e("SelectItemFragment", "Product ID Error: itemId = -1");

                }

                database.insertNewUPC(upcSaved, name, description, itemId);


                // stuff everything in a bundle in case it's needed for PrintedExpirationDate; TODO rip this out if it's not needed
                /*Bundle saved = new Bundle();
                saved.putInt("savedID", subCategory);
                saved.putString("savedUPC", upcSaved);
                saved.putString("savedProductName", name);
                saved.putString("savedDescription", description);
                getParentFragmentManager().setFragmentResult("PRODUCT INFO", saved);*/

                Bundle bundle = new Bundle();

                NestUPC foodItem = database.getNestUPC(upcSaved);

                bundle.putSerializable("foodItem", foodItem);

                // Need to clear the result with the same request key, before using possibly same request key again.
                getParentFragmentManager().clearFragmentResult("FOOD ITEM");

                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

                // navigate over to printed expiration date;
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.action_CE_SelectItemFragment_to_SelectPrintedExpirationDateFragment);

            }
        });

        // CANCEL BUTTON CODE - NAVIGATE BACK TO START FRAGMENT
        view.findViewById(R.id.cancelButton).setOnClickListener( view12 ->
            NavHostFragment.findNavController(SelectItemFragment.this).navigate(R.id.CE_StartFragment));

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
            menu.add(i, i, i, mainCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item ->
        {
            // set a text view with the category for the user to see;
            categoryHint.setText( item.toString() );
            categoryIndex = item.getItemId();

            // clear out subcategory between menu changes;
            productHint.setText(" ");
            subCategory =  -1;
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
        if (categoryIndex == -1)
            Toast.makeText(getContext(), "Please narrow the choices with a main category first!", Toast.LENGTH_LONG).show();

        // SHOW THE POP UP MENU FOR THE SUB CATEGORIES, BY USING A POPUP MENU
        PopupMenu menuPop = new PopupMenu(getContext(), productButton);
        Menu menu = menuPop.getMenu();

        String[] subCategories = getResources().getStringArray( categories[categoryIndex] );

        for (int i = 0; i < subCategories.length; ++i )
            menu.add(categoryIndex, i, i, subCategories[i]);

        // THE ACTUAL ON CLICK CODE TO SET THE PRODUCT ID AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener( item ->
        {
            productHint.setText( item.toString() );
            subCategory = item.getItemId();
            return true;
        });

        menuPop.show();
    }

    /** UNUSED METHODS - NEEDED FOR INTERFACES THAT ARE IMPLEMENTED **/
    @Override public void onClick(View v) { }


}