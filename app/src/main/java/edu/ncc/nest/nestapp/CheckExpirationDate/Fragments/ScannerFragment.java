package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/**
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
 *
 * Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;

import java.util.Objects;
import java.util.concurrent.Future;

import edu.ncc.nest.nestapp.AbstractScannerFragment.AbstractScannerFragment;
import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;

/**
 * ScannerFragment: Used to scan in a UPC barcode, and send it to the appropriate fragment depending
 * on whether or not a product with the scanned barcode exist in the database. This class performs
 * the initial loading of the Nest.db database using {@link BackgroundTask}.
 *
 * Navigates to {@link ConfirmItemFragment} with the item pulled from database, if the upc exists in
 * the local database.
 *
 * Navigates to {@link SelectItemFragment} with the barcode, if the upc does not exist in the local
 * database.
 */
public class ScannerFragment extends AbstractScannerFragment {

    public static final String LOG_TAG = ScannerFragment.class.getSimpleName();

    private final TaskHelper taskHelper = new TaskHelper(1);

    private Future<NestDBDataSource> future = null;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure we don't set formats until our super has a handle on the DecoratedBarcodeView
        super.setDecoderFormats(BarcodeFormat.UPC_A);

    }

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        // Log the barcode result and format
        Log.d(LOG_TAG, "Scan Confirmed: [" + barcode + ", " + format.toString() + "]");

        if (future == null) {

            // Submit a new LoadDatabaseTask to the helper, store the resulting future
            future = taskHelper.submit(new LoadDatabaseTask(barcode) {

                @Override
                protected void onError(@NonNull Throwable throwable) {
                    super.onError(throwable);

                    // Clear the future object since this original task failed
                    future = null;

                }

            });

        } else

            // Execute a new task that retrieves the database from the original submitted task
            taskHelper.execute(new LoadDatabaseTask(barcode) {

                @Override
                protected NestDBDataSource doInBackground() throws Exception {

                    // Retrieve the database from the previously submitted task
                    return future.get();

                }

            });

    }

    /**
     * Called from {@link LoadDatabaseTask#onPostExecute} after the Nest.db database has been
     * successfully loaded/created, and after the user has been informed that the database has been
     * successfully loaded/created.
     *
     * @param nestDBDataSource The database source object used to interact with the database
     * @param confirmedBarcode The barcode that was confirmed when the task was created
     */
    private void onSuccessfulDatabaseLoad(@NonNull NestDBDataSource nestDBDataSource,
                                          @NonNull String confirmedBarcode) {

        // Find the NestUPC object that matches the scanned barcode
        NestUPC result = nestDBDataSource.getNestUPC(confirmedBarcode);

        FragmentManager fragmentManager = ScannerFragment.this.getParentFragmentManager();

        Bundle bundle = new Bundle();

        if (result != null) {

            // If we get here, then the upc is already in the database.

            Log.d(ScannerFragment.LOG_TAG, "Result returned: " + result.getUpc() + " " + result.getProductName());

            // Put the item in a bundle and pass it to ConfirmItemFragment
            bundle.putSerializable("foodItem", result);

            // Make sure there is no result currently set for this request key
            fragmentManager.clearFragmentResult("FOOD ITEM");

            fragmentManager.setFragmentResult("FOOD ITEM", bundle);

            // Navigate to ConfirmItemFragment
            NavHostFragment.findNavController(ScannerFragment.this)
                    .navigate((R.id.CED_ConfirmItemFragment));

        } else {

            // If we get here, then the upc is does not exist in the database.

            // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
            bundle.putString("barcode", confirmedBarcode);

            // Make sure there is no result currently set for this request key
            fragmentManager.clearFragmentResult("BARCODE");

            fragmentManager.setFragmentResult("BARCODE", bundle);

            // Navigate to SelectItemFragment
            NavHostFragment.findNavController(ScannerFragment.this)
                    .navigate((R.id.CED_SelectItemFragment));

        }

    }

    /**
     * {@link BackgroundTask} that performs the initial loading/creation of the Nest.db database.
     */
    private class LoadDatabaseTask extends BackgroundTask<Void, NestDBDataSource> {

        /** Holds a reference to the barcode that was confirmed */
        private final String confirmedBarcode;

        /** Holds a reference to the AlertDialog that informs the user that this task is running. */
        private AlertDialog loadDialog;

        /**
         * Constructs a LoadDatabaseTask and creates a loading dialog then shows it.
         *
         * @param barcode The barcode that was confirmed
         */
        public LoadDatabaseTask(@NonNull String barcode) {

            this.confirmedBarcode = Objects.requireNonNull(barcode);

            // Create a loading dialog and then show it

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ScannerFragment.this.requireContext());

            // Set the positive button's listener to null so we can use it later
            builder.setPositiveButton("DISMISS", null);

            builder.setView(R.layout.dialog_loading_database);

            builder.setCancelable(false);

            (loadDialog = builder.create()).show();

            // Hide the "dismiss" button until it is needed
            loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);

        }

        @Override
        protected NestDBDataSource doInBackground() throws Exception {

            // Open a database object so we can check whether the barcode exist in the database
            return new NestDBDataSource(requireContext());

        }

        @Override
        protected final void onPostExecute(NestDBDataSource nestDBDataSource) {

            try {

                // Switch the displayed view to the first child view
                ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper)).setDisplayedChild(1);

                // Display the positive button to the user
                loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        "DISMISS", (dialog, which) -> {

                    try {

                        // Send the result to the outer ScannerFragment class
                        ScannerFragment.this.onSuccessfulDatabaseLoad(nestDBDataSource,
                                confirmedBarcode);

                    } finally {

                        dialog.dismiss();

                    }

                });

            } finally {

                // Set loading dialog to null to prevent Context leaks
                loadDialog = null;

            }

        }

        @Override
        @CallSuper
        protected void onError(@NonNull Throwable throwable) {
            super.onError(throwable);

            try {

                // Switch the displayed view to the second child view
                ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper)).setDisplayedChild(2);

                // Display the positive button to the user
                loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                        "DISMISS", (dialog, which) -> {

                    try {

                        // Resume scanning if outer ScannerFragment class is resumed
                        if (ScannerFragment.this.isResumed())

                            ScannerFragment.this.onResume();

                    } finally {

                        dialog.dismiss();

                    }

                });

            } finally {

                // Set loading dialog to null to prevent Context leaks
                loadDialog = null;

            }

        }

    }

}