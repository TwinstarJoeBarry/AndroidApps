package edu.ncc.nest.nestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class NestNewUPC extends AppCompatActivity {
    private String upcBeingAdded;
    private int selectedCategoryId, selectedProductId;
    private EditText brandEdit, descriptionEdit;
    private Spinner categorySpinner, productSpinner;
    private NestDBDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_new_upc);

        // get the new UPC code and display it in addingLabel
        Intent intent = getIntent();
        upcBeingAdded = intent.getStringExtra("upc");
        TextView addingLabel = findViewById(R.id.nest_new_upc_adding_label);
        addingLabel.setText(String.format(Locale.getDefault(),addingLabel.getText().toString(), upcBeingAdded));

        // get view references
        brandEdit = findViewById(R.id.brandEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        categorySpinner = findViewById(R.id.categorySpinner);
        productSpinner = findViewById(R.id.productSpinner);

        // open the database
        dataSource = new NestDBDataSource(this);

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
        dataSource.insertNewUPC(upcBeingAdded, brand, description, selectedProductId);

        setResult(RESULT_OK);
        finish();
    }

    public void cancelClicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}