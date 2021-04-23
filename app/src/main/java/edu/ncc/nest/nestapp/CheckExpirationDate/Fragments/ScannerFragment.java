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

    private final TaskHelper taskHelper = new TaskHelper(2);

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
     * {@link BackgroundTask} that performs the initial loading/creation of the Nest.db database.
     */
    private class LoadDatabaseTask extends BackgroundTask<Void, NestDBDataSource> {

        /** Holds a reference to the barcode that was confirmed */
        private final String confirmedBarcode;

        /** Holds a reference to the AlertDialog that informs the user that this task is running. */
        private AlertDialog loadingDialog;

        /**
         * Constructs a LoadDatabaseTask and creates a loading dialog then shows it.
         *
         * @param barcode The barcode that was confirmed
         */
        public LoadDatabaseTask(@NonNull String barcode) {

            this.confirmedBarcode = Objects.requireNonNull(barcode);

            // Create a loading dialog and then show it
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setView(R.layout.dialog_loading);

            builder.setCancelable(false);

            loadingDialog = builder.create();

            loadingDialog.show();

        }

        @Override
        protected NestDBDataSource doInBackground() throws Exception {

            // Open a database object so we can check whether the barcode exist in the database
            return new NestDBDataSource(requireContext());

        }

        @Override
        protected void onPostExecute(NestDBDataSource nestDBDataSource) {

            Log.d(LOG_TAG, "In ScannerFragment.LoadDatabaseTask's onPostExecute method");

            try {

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

            } finally {

                LoadDatabaseTask.this.dispose();

            }

        }

        @Override
        protected void onError(@NonNull Throwable throwable) {
            super.onError(throwable);

            // Make sure we dispose of loadingDialog first
            LoadDatabaseTask.this.dispose();

            // Create a error dialog informing the user there was an error and then show it
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            builder.setTitle("Error loading database!");

            builder.setMessage("There was an error loading the database, please try again." +
                    "\n\nSee log for more info.");

            builder.setPositiveButton("DISMISS", (dialog, which) -> {

                dialog.dismiss();

                // Resume scanning if outer ScannerFragment class is resumed
                if (ScannerFragment.this.isResumed())

                    ScannerFragment.this.onResume();

            });

            builder.setOnCancelListener(dialog -> {

                // Resume scanning if outer ScannerFragment class is resumed
                if (ScannerFragment.this.isResumed())

                    ScannerFragment.this.onResume();

            });

            builder.setCancelable(true);

            builder.create().show();

        }

        /**
         * Disposes of any UI objects that may be holding onto a Context reference.
         */
        protected void dispose() {

            if (loadingDialog != null) {

                loadingDialog.dismiss();

                loadingDialog = null;

            }

        }

    }

}