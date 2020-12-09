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
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;






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


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        /** CATEGORY SPINNER - SET UP ARRAY ADAPTER LIST */
        Spinner categorySpinner = (Spinner)view.findViewById(R.id.categorySpinner);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource( getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item );

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(this);


        /** ACCEPT BUTTON CODE - PARSE AND ADD VALES TO NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE */
        view.findViewById(R.id.acceptButton).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                // open the database to parse the information
                database = new NestDBDataSource(getContext());
                String upcBeingAdded = "0123456789AB"; // TODO need to get this from scan/enter upc frag through bundle savedInstance

                // retrieve the item information from each view, casting as appropriate
                name = ((EditText) (getView().findViewById(R.id.brandEdit))).getText().toString();
                description = ((EditText) (getView().findViewById(R.id.descriptionEdit))).getText().toString();

//                Spinner categorySpinner = ((Spinner) (getView().findViewById(R.id.categorySpinner)));
//                Spinner productSpinner = ((Spinner) (getView().findViewById(R.id.productSpinner)));
//                int selectedCategoryId = 0, selectedProductId = 0; // need top ull these from array adapter


                // make sure all fields were was available and entered properly
                if (name.isEmpty() || description.isEmpty() )
                    Toast.makeText(getContext(), "Please fill or select all fields!", Toast.LENGTH_LONG).show();

                else
                {
                    // use dataSource to add the new upc to the Nest UPCs table
                    Toast.makeText(getContext(), String.format("name: %s des: %s", name, description), Toast.LENGTH_LONG).show();
//                    database.insertNewUPC(upcBeingAdded, name, description, product);


                    // stuff everything in a bundle in case it's needed for PrintedExpirationDate
                    Bundle saved = new Bundle();

//                    String StringInfo[] = {name, description};
//                    saved.putStringArray("savedStringInfo", StringInfo);
//                    getParentFragmentManager().setFragmentResult("savedStringInfo", saved);
                        // fragment result listener
//                    int intInfo[] = {category, product};
//                    saved.putIntArray("savedIntInfo", intInfo);
//                    getParentFragmentManager().setFragmentResult("savedIntInfo", saved);

                    NavHostFragment.findNavController(SelectItemFragment.this)
                            .navigate(R.id.action_selectItemFragment_to_selectPrintedExpirationDateFragment);
                }
            }
        });


        /** CANCEL BUTTON CODE - NAVIGATE BACK TO ENTER UPC FRAG */
        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                NavHostFragment.findNavController(SelectItemFragment.this)
                        .navigate(R.id.confirmItemFragment);
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