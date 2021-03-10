package edu.ncc.nest.nestapp.AsyncTask;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TaskExecutor --
 * Used to execute a BackgroundTask, and handles the lifecycle of a BackgroundTask.
 */
@SuppressWarnings("unused")
public final class TaskExecutor {

    // Create a ExecutorService that can use multiple threads to execute tasks, best used for database reads.
    private static final ExecutorService READ_EXECUTOR_SERVICE =
            new ThreadPoolExecutor(4, 16,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    // Create a ExecutorService that will execute one task at a time, best used for database writes.
    private static final ExecutorService WRITE_EXECUTOR_SERVICE =
            Executors.newSingleThreadExecutor();

    // Get a Handler for the Main UI thread
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    ////////////////////////////////////// LIFECYCLE METHODS ///////////////////////////////////////

    /**
     * onExecute --
     * Called when executing a BackgroundTask. Handles the lifecycle of the BackgroundTask that is
     * being executed.
     * @param TASK The BackgroundTask to execute.
     * @param <Progress> The type that will be used to represent a BackgroundTask's progress.
     * @param <Result> The type that will be used as a return value after executing the BackgroundTask
     */
    private static <Progress, Result> void onExecute(final BackgroundTask<Progress, Result> TASK) {

        boolean taskFailed = false;

        Result result = null;

        try {

            // Execute the background code
            result = TASK.doInBackground();

        } catch (Exception e) {

            // If we get here the BackgroundTask has failed to fully execute
            taskFailed = true;

            // Call this lifecycle method on the Main UI thread
            MAIN_HANDLER.post(() -> TASK.onTaskFailed(e));

        } finally {

            if (!taskFailed) {

                final Result RESULT = result;

                // If the BackgroundTask hasn't failed, call this lifecycle method on the Main UI thread
                MAIN_HANDLER.post(() -> TASK.onPostExecute(RESULT));

            }

        }

    }

    /////////////////////////////////////// ACTION METHODS /////////////////////////////////////////



    //////////////////////////////////// GETTERS AND SETTERS ///////////////////////////////////////

    /**
     * getMainHandler --
     * Returns the Handler of this class that uses the Main UI thread
     * @return The Handler of this class that uses the Main UI thread
     */
    public static Handler getMainHandler() {

        return MAIN_HANDLER;

    }

}
