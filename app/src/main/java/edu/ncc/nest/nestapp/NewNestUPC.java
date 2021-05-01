package edu.ncc.nest.nestapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Locale;

import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;

public class NewNestUPC extends NestDBDataSource.NestDBActivity {
    private String upcBeingAdded;
    private int selectedCategoryId, selectedProductId;
    private EditText brandEdit, descriptionEdit;
    private Spinner categorySpinner, productSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) {
        super.onLoadSuccess(nestDBDataSource);

        // If the activity is not dead
        if (!this.isDestroyed()) {

            setContentView(R.layout.activity_new_nest_upc);

            // get the new UPC code and display it in addingLabel
            Intent intent = getIntent();
            upcBeingAdded = intent.getStringExtra("upc");
            TextView addingLabel = findViewById(R.id.fragment_select_item_headline);
            addingLabel.setText(String.format(Locale.getDefault(), addingLabel.getText().toString(), upcBeingAdded));

            // get view references
            brandEdit = findViewById(R.id.fragment_select_item_brand_entry);
            descriptionEdit = findViewById(R.id.fragment_select_item_description_entry);
            categorySpinner = findViewById(R.id.categorySpinner);
            productSpinner = findViewById(R.id.productSpinner);

        }

    }

    @Override
    protected void onLoadError(@NonNull Throwable throwable) {
        super.onLoadError(throwable);

    }

    public void acceptClicked(View view) {
        String brand = brandEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        if (brand.isEmpty() ||
            description.isEmpty() ||
            selectedCategoryId == 0 ||
            selectedProductId == 0) {
            Toast.makeText(this, "Please fill or select all fields!", Toast.LENGTH_LONG).show();
            return;
        }
        // use dataSource to add the new upc to the Nest UPCs table
        requireDataSource().insertNewUPC(upcBeingAdded, brand, description, selectedProductId);

        setResult(RESULT_OK);
        finish();
    }

    public void cancelClicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
