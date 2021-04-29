package edu.ncc.nest.nestapp.nestdb;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = NestDBActivity.class.getSimpleName();

    /** TaskHelper object that uses a single thread executor */
    private final TaskHelper taskHelper = new TaskHelper();

    private NestDBDataSource nestDBDataSource = null;

    private Future<NestDBDataSource> future = null;

    private static final long LOAD_DELAY = 1250L;

    ////////////////////////////////////// LIFECYCLE METHODS ///////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nest_db);

        setSupportActionBar(findViewById(R.id.nest_db_toolbar));

        // If a task hasn't been submitted or an existing task has been cancelled before it finished
        if (future == null || future.isCancelled()) {

            // Create a loading dialog to display to the user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Set the positive button's listener to null so we can use it later
            builder.setPositiveButton("DISMISS", null);

            builder.setView(R.layout.dialog_loading_database);

            // Make sure this is dialog is not cancelable so the user is forced to wait
            builder.setCancelable(false);

            final AlertDialog loadDialog = builder.create();

            loadDialog.show();

            // Hide the positive button of the dialog until it is needed
            loadDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);

            // Submit a new BackgroundTask to the helper that loads the database, store the resulting future
            future = taskHelper.submit(new BackgroundTask<Void, NestDBDataSource>() {

                @Override
                @WorkerThread
                protected NestDBDataSource doInBackground() throws Exception {

                    // This allows the user to see the loading bar for a short time
                    // Optional: This can be removed if not needed/desired
                    Thread.sleep(LOAD_DELAY);

                    return new NestDBDataSource(NestDBActivity.this);

                }

                @Override
                @MainThread
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
                @MainThread
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
     * loaded/created.
     *
     * <p><br>NOTE: Use this method instead of {@link #onCreate(Bundle)} to set the content view</p>
     *
     * @param nestDBDataSource The database source object used to interact with the database
     */
    protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) { }

    /**
     * Called after the user has been informed that there was an error loading the Nest.db database.
     * Calls {@link #finish()} on this Activity. Can be overridden for additional functionality.
     */
    protected void onLoadError(@NonNull Throwable throwable) {

        Log.w(LOG_TAG, "Finishing activity due to load error");

        finish();

    }

    /////////////////////////////////////// CLASS METHODS //////////////////////////////////////////

    /**
     * Returns the {@link NestDBDataSource} object stored in the {@code nestDBDataSource} instance
     * variable.
     *
     * <p><br>Note: Checking the return value of isDatabaseLoaded() before calling this
     * method will help ensure that this method will not throw a {@link NullPointerException}.</p>
     *
     * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
     * variable
     *
     * @see #isDatabaseLoaded()
     */
    @NonNull
    public final NestDBDataSource requireDataSource() {

        return Objects.requireNonNull(nestDBDataSource,
                "The Nest.db database has not been successfully loaded yet");

    }

    /**
     * Static version of requireDataSource(). Returns the {@link NestDBDataSource} object stored in
     * the {@link NestDBActivity} that is associated with the given {@link Fragment}.
     *
     * <p><br>Note: Checking the return value of isDatabaseLoaded() before calling this
     * method will help ensure that this method will not throw a {@link NullPointerException}.</p>
     *
     * @param fragment {@link Fragment} that is associated with a {@link NestDBActivity}
     * @return the {@link NestDBDataSource} object stored in {@code nestDBDataSource} instance
     * variable
     * @throws ClassCastException If the FragmentActivity associated with the given Fragment is not
     * an instance of {@link NestDBActivity}
     *
     * @see #requireDataSource()
     * @see #isDatabaseLoaded()
     */
    @NonNull
    public static NestDBDataSource requireDataSource(@NonNull Fragment fragment) {

        FragmentActivity fragmentActivity = fragment.requireActivity();

        if (fragmentActivity instanceof NestDBActivity)

            return ((NestDBActivity) fragmentActivity).requireDataSource();

        throw new ClassCastException("The FragmentActivity this Fragment is currently associated" +
                " with is not an instance of " + NestDBActivity.class.getCanonicalName());

    }

    /**
     * Determines whether or not the task has finished. Does NOT determine if the task was
     * successful or not.
     *
     * @return {@code true} if the task has finished, {@code false} otherwise
     */
    public final boolean taskFinished() {

        return (future != null && future.isDone());

    }

    /**
     * Determines whether or not the Nest.db database has been successfully loaded.
     *
     * @return {@code true} if the database was successfully loaded, {@code false} otherwise
     */
    public final boolean isDatabaseLoaded() {

        return (taskFinished() && nestDBDataSource != null);

    }

}
