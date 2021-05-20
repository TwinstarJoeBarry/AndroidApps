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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Objects;

import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectItemFragment: Allows user to select item options that will be used to determine a proper
 * product id for the provided barcode. The barcode is then added into the local database and
 * the resulting item is sent to {@link SelectPrintedExpirationDateFragment}.
 *
 * See {@link edu.ncc.nest.nestapp.NewNestUPC} and {@link edu.ncc.nest.nestapp.ItemInformation}
 * for reference.
 */
public class SelectItemFragment extends Fragment {

    /** CONSTANT DEFAULTS **/
    // FOR NON ESSENTIAL TEXT ENTRY VIEWS THAT WERE OPTIONAL AND INTENTIONALLY LEFT BLANK;
    private final String DEFAULT_STRING = "[LEFT BLANK]";
    // FOR ESSENTIAL TEXT ENTRY VIEWS THAT SHOULD NEVER BE BLANK AND REPLACED IN CODE
    private final String PLACEHOLDER_STRING = "[NOT RECEIVED]";
    NestDBDataSource dataSource = new NestDBDataSource(this.getContext());

    /** INSTANCE VARS **/
    private String upcString; // UPC as passed from previous fragments (populated with saved bundle)

    // To access UI elements; both directly and indirectly
    Button categoryButton, productButton, subtitleButton;
    TextView categoryHint, productHint, subtitleHint;

    private String productName, productSubtitle;
    private int productCategoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize variables; placeholder values replaced later
        upcString = PLACEHOLDER_STRING;

        productCategoryId = -1;
        productName = null;
        productSubtitle = null;

        return inflater.inflate(R.layout.fragment_check_expiration_date_select_item,
                container, false);

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INITIALIZE UI ELEMENTS THAT ARE INSTANCE VARIABLES
        categoryHint = view.findViewById(R.id.fragment_select_item_category_hint);
        categoryButton = view.findViewById(R.id.fragment_select_item_category_select);
        categoryButton.setOnClickListener( v -> showCategories() );

        productHint = view.findViewById(R.id.fragment_select_item_product_hint);
        productButton = view.findViewById(R.id.fragment_select_item_product_select);
        productButton.setOnClickListener( v -> showProducts() );
        productButton.setEnabled(false);

        subtitleHint = view.findViewById(R.id.fragment_select_item_subtitle_hint);
        subtitleButton = view.findViewById(R.id.fragment_select_item_subtitle_select);
        subtitleButton.setOnClickListener( v -> showProductSubtitles(
                productCategoryId, productName));
        subtitleButton.setEnabled(false);


        // GRAB THE UPC VALUES FROM THE BUNDLE SENT FROM SCANNER FRAGMENT OR CONFIRM ITEM FRAGMENT
        getParentFragmentManager().setFragmentResultListener("BARCODE", this, (key, bundle) -> {

            upcString = bundle.getString("barcode");

            if (!upcString.equals(PLACEHOLDER_STRING)) {

                String text = "Adding UPC: " + upcString;

                ((TextView) view.findViewById(R.id.fragment_select_item_headline)).setText(text);

            } else {

                // FIXME Nothing to get when navigating backward from next fragment
                //  (select printed expiration date); not likely desirable behavior!

                throw new RuntimeException("Failed to retrieve UPC barcode.");

            }

            // Clear the result listener since we have successfully retrieved the result
            getParentFragmentManager().clearFragmentResultListener("BARCODE");

        });

        // ACCEPT BUTTON CODE - PARSE VALUES FOR NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.acceptButton).setOnClickListener( view1 -> {

            // Retrieve the String information from each view, casting as appropriate;
            String brand = ((EditText) (view.findViewById(
                    R.id.fragment_select_item_brand_entry))).getText().toString();

            String description = ((EditText) (view.findViewById(
                    R.id.fragment_select_item_description_entry))).getText().toString();

            // Replace any fields from above with blank values;
            if ( brand.isEmpty() )
                brand = DEFAULT_STRING;

            if ( description.isEmpty() )
                description = DEFAULT_STRING;

            // and first assert that the values that CANNOT be blank, are not;
            if ((productCategoryId == -1) || (productButton.isEnabled() && productName == null) ||
                    (subtitleButton.isEnabled() && productSubtitle == null)) {

                Toast.makeText(getContext(), "Don't forget to select a category, name, and " +
                        "subtitle-(if required)!", Toast.LENGTH_LONG).show();

            } else {

                // Use our source to the database to add the new upc to the NEST's table after parsing the ID
                // NOTE: This will return -1 if the UPC has never been added before and will cause errors in future fragments.
                if (dataSource.getProductIdFromUPC(upcString) == -1) {

                    // No productId associated with this upc in the database

                    Log.d("SelectItemFragment", "Selected Product: " + productCategoryId +
                            ", " + productName + ", " + productSubtitle);

                    int productId = dataSource.getProdIdfromProdInfo(
                            productCategoryId, productName, productSubtitle);

                    Log.d("SelectItemFragment", "Product ID: " + productId);

                    if (dataSource.insertNewUPC(upcString, brand, description, productId) == -1)

                        throw new RuntimeException("Error inserting new UPC");

                } else {

                    // A productId is already associated with this upc in the database.

                    int productId = dataSource.getProdIdfromProdInfo(
                            productCategoryId, productName, productSubtitle);

                    // TODO Update the UPC stored in the database with the new brand, description,
                    //  and productId

                    // Adding this exception for now to prevent errors
                    throw new RuntimeException("NestUPC exists. Need to update upc in database.");

                }

                Bundle bundle = new Bundle();

                NestUPC foodItem = dataSource.getNestUPC(upcString);

                if (foodItem == null)

                    throw new RuntimeException("Error retrieving NestUPC");

                Log.d("SelectItemFragment", "foodItem.toString(): " + foodItem.toString());

                bundle.putSerializable("foodItem", foodItem);

                // Need to clear the result with the same request key, before using same request key again.
                getParentFragmentManager().clearFragmentResult("FOOD ITEM");

                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

                // Navigate over to SelectPrintedExpirationDateFragment;
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.action_CED_SelectItemFragment_to_SelectPrintedExpirationDateFragment);

            }

        });

        // CANCEL BUTTON CODE - NAVIGATE BACK TO START FRAGMENT
        view.findViewById(R.id.cancelButton).setOnClickListener( view12 ->
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.CED_StartFragment));

    }


    /**
     * showCategories --
     * Creates a PopupMenu to display a list of categories for the user to choose from.
     **/
    private void showCategories() {

        PopupMenu menuPop = new PopupMenu(getContext(), categoryButton);

        Menu menu = menuPop.getMenu();

        ArrayList<String> categories = dataSource.getCategories();

        for (int i = categories.size() - 1; i >= 0; i--)

            menu.add(i + 1, i, i, categories.get(i));

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item -> {

            productCategoryId = item.getItemId() + 1;

            // Set a text view with the category for the user to see;
            categoryHint.setText(item.toString());

            clearSubtitleSelection();

            subtitleButton.setEnabled(false);

            productButton.setEnabled(true);

            productHint.setText("");

            productName = null;

            return true;

        });

        menuPop.show();

    }



    /**
     * showProduct --
     * Creates a PopupMenu to display a list of products for the user to choose from, as long as a
     * category has been selected.
     **/
    private void showProducts() {

        if (productCategoryId != -1) {

            PopupMenu menuPop = new PopupMenu(getContext(), productButton);

            Menu menu = menuPop.getMenu();

            final ArrayList<String> products = dataSource.getProductNames(productCategoryId);

            for (int i = products.size() - 1; i >= 0; i--)

                menu.add(productCategoryId, i, i, products.get(i));


            // THE ACTUAL ON CLICK CODE TO SET THE SUBCATEGORY AND POPULATE A TEXT VIEW WITH THE INFORMATION
            menuPop.setOnMenuItemClickListener(item -> {

                productName = item.toString();

                productHint.setText(productName);

                clearSubtitleSelection();

                subtitleButton.setEnabled(!dataSource.getProductSubtitles(
                        productCategoryId, productName).isEmpty());

                return true;

            });

            menuPop.show();

        } else

            Toast.makeText(getContext(), "Please narrow the choices with a main category first!", Toast.LENGTH_LONG).show();

    }

    /**
     * showProductSubtitles --
     *
     * Creates a PopupMenu to display a list of subtitles for the user to choose from, as long as a
     * product has been selected.
     *
     * @param categoryId
     * @param productName
     */
    private void showProductSubtitles(int categoryId, @NonNull String productName) {

        PopupMenu menuPop = new PopupMenu(getContext(), subtitleButton);

        Menu menu = menuPop.getMenu();

        ArrayList<String> subtitles = dataSource.getProductSubtitles(categoryId,
                Objects.requireNonNull(productName));

        for (int i = subtitles.size() - 1; i >= 0; i--)

            menu.add(i, i, i, subtitles.get(i));

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item -> {

            productSubtitle = item.toString();

            // Set a text view with the subtitle for the user to see;
            subtitleHint.setText(productSubtitle);

            return true;

        });

        menuPop.show();

    }

    /**
     * Clears the subtitle selection
     */
    private void clearSubtitleSelection() {

        subtitleHint.setText("");

        productSubtitle = null;

    }

}
