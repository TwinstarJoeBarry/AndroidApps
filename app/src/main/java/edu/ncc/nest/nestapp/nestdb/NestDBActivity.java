package edu.ncc.nest.nestapp.nestdb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.Future;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.async.BackgroundTask;
import edu.ncc.nest.nestapp.async.TaskHelper;

/**
 * Loads a {@link NestDBDataSource} object using a {@link BackgroundTask} when this Activity is
 * created ({@link NestDBActivity#onCreate}). Also has a method that returns the
 * fully-loaded/non-{@code null} {@link NestDBDataSource} object.
 */
public abstract class NestDBActivity extends AppCompatActivity {

    /////////////////////////////////////// CLASS VARIABLES ////////////////////////////////////////

    public static final String LOG_TAG = NestDBActivity.class.getSimpleName();

    /** TaskHelper object that uses a single thread executor */
    private final TaskHelper taskHelper = new TaskHelper();

    private NestDBDataSource nestDBDataSource = null;

    private Future<NestDBDataSource> future = null;

    private static final long LOAD_DELAY = 1500L;

    ////////////////////////////////////// LIFECYCLE METHODS ///////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a loading dialog to display to the user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the positive button's listener to null so we can use it later
        builder.setPositiveButton("DISMISS", null);

        builder.setView(R.layout.dialog_loading_database);

        // Make sure this is dialog is not cancelable so the user is forced to wait
        builder.setCancelable(false);

        AlertDialog loadDialog = builder.create();

        loadDialog.show();

        // Hide the positive button of the dialog until it is needed
        loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);

        // If a task hasn't been submitted or an existing task has been cancelled
        if (future == null || future.isCancelled()) {

            // Submit a new BackgroundTask to the helper that loads the database, store the resulting future
            future = taskHelper.submit(new BackgroundTask<Void, NestDBDataSource>() {

                @Override
                protected NestDBDataSource doInBackground() throws Exception {

                    try {

                        // This allows the user to see the loading bar for a short time
                        // Optional: This can be removed if not needed/desired
                        Thread.sleep(LOAD_DELAY);

                    } catch (InterruptedException e) {

                        Log.w(LOG_TAG, "Non-Fatal task exception occurred");

                        Log.w(LOG_TAG, Log.getStackTraceString(e));

                    }

                    return new NestDBDataSource(NestDBActivity.this);

                }

                @Override
                protected void onPostExecute(NestDBDataSource nestDBDataSource) {

                    // Update our instance variable since the database was successfully loaded
                    NestDBActivity.this.nestDBDataSource = nestDBDataSource;

                    // Switch the displayed view to the first child view
                    ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper))
                            .setDisplayedChild(1);

                    // Display the positive button to the user
                    loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                    loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            "DISMISS", (dialog, which) -> {

                                try {

                                    // Send the result to the outer class
                                    NestDBActivity.this.onLoadSuccess(nestDBDataSource);

                                } finally {

                                    dialog.dismiss();

                                }

                            });

                }

                @Override
                protected void onError(@NonNull Throwable throwable) {
                    super.onError(throwable);

                    // Failed to load the database so make sure source object is set to null
                    NestDBActivity.this.nestDBDataSource = null;

                    // Switch the displayed view to the second child view
                    ((ViewFlipper) loadDialog.findViewById(R.id.dialog_flipper))
                            .setDisplayedChild(2);

                    // Display the positive button to the user
                    loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);

                    loadDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                            "DISMISS", (dialog, which) -> {

                                try {

                                    // Send the error to the outer NestDBActivity class
                                    NestDBActivity.this.onLoadError(throwable);

                                } finally {

                                    dialog.dismiss();

                                }

                            });

                }

            });

        }

    }

    /**
     * Called after the user has been informed that the Nest.db database has been successfully
     * loaded/created. Can be overridden for additional functionality.
     *
     * @param nestDBDataSource The database source object used to interact with the database
     */
    protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) {

    }

    /**
     * Called after the user has been informed that there was an error loading the Nest.db database.
     * Can be overridden for additional functionality.
     *
     * NOTE: This method is called after the user is informed (by an {@link AlertDialog}) of the
     * error.
     */
    protected void onLoadError(@NonNull Throwable throwable) {

        throw new RuntimeException("Error loading Nest.db database", throwable);

    }

    /////////////////////////////////////// CLASS METHODS //////////////////////////////////////////

    /**
     * Returns the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
     * variable.
     *
     * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
     * variable
     * @throws NullPointerException If the object is {@code null}/not-yet-loaded
     */
    @NonNull
    public final NestDBDataSource requireDataSource() {

        return Objects.requireNonNull(nestDBDataSource, "nestDBDataSource is null");

    }

}
