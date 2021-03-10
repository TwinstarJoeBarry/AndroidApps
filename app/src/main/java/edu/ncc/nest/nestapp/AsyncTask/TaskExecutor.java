package edu.ncc.nest.nestapp.AsyncTask;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TaskExecutor --
 * Used to execute a BackgroundTask object, and handles the lifecycle of the BackgroundTask as it
 * executes.
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
     * @param EXECUTOR_SERVICE The ExecutorService to use to execute a BackgroundTask.
     * @param <Progress> The data type that will represent the "progress" of the BackgroundTask.
     * @param <Result> The data type that will represent the "result" of the BackgroundTask.
     */
    private static <Progress, Result> void onExecute(final BackgroundTask<Progress, Result> TASK,
                                                     final ExecutorService EXECUTOR_SERVICE) {

        // Call this lifecycle method on the current thread
        TASK.onPreExecute(); // (May need update this to post to the MAIN_HANDLER instead)

        // Execute the following code using the EXECUTOR_SERVICE provided
        EXECUTOR_SERVICE.execute(() -> {

            Result result = null;

            boolean taskFailed = false;

            try {

                // Execute the background code, and get the "result"
                result = TASK.doInBackground();

            } catch (Exception e) {

                // If we get here the BackgroundTask has failed to fully execute
                taskFailed = true;

                // Call this lifecycle method on the Main UI thread
                MAIN_HANDLER.post(() -> TASK.onTaskFailed(e));

            } finally {

                // If the BackgroundTask hasn't failed
                if (!taskFailed) {

                    final Result RESULT = result;

                    // Call this lifecycle method on the Main UI thread
                    MAIN_HANDLER.post(() -> TASK.onPostExecute(RESULT));

                }

            }

        });

    }

    /////////////////////////////////////// ACTION METHODS /////////////////////////////////////////

    /**
     * executeAsRead --
     * Optimized for executing BackgroundTask that will do database reads more efficiently.
     * @param TASK The BackgroundTask to execute.
     * @param <Progress> The data type that will represent the "progress" of the BackgroundTask.
     * @param <Result> The data type that will represent the "result" of the BackgroundTask.
     */
    public static <Progress, Result> void executeAsRead(
            @NonNull final BackgroundTask<Progress, Result> TASK) {

        onExecute(TASK, READ_EXECUTOR_SERVICE);

    }

    /**
     * executeAsWrite --
     * Optimized for executing BackgroundTask that will do database writes more safely.
     * @param TASK The BackgroundTask to execute.
     * @param <Progress> The data type that will represent the "progress" of the BackgroundTask.
     * @param <Result> The data type that will represent the "result" of the BackgroundTask.
     */
    public static <Progress, Result> void executeAsWrite(
            @NonNull final BackgroundTask<Progress, Result> TASK) {

        onExecute(TASK, WRITE_EXECUTOR_SERVICE);

    }

    //////////////////////////////////// BACKGROUND TASK CLASS /////////////////////////////////////

    /**
     * BackgroundTask --
     * Represent a task that can be executed on a background thread.
     * @param <Progress> The data type that will represent the "progress" of this task.
     * @param <Result> The data type that will represent the "result" of this task.
     */
    public abstract static class BackgroundTask<Progress, Result> {

        // Useful for updating UI as a task progresses
        private OnProgressListener<Progress> onProgressListener = null;

        /////////////////////////////// ON PROGRESS LISTENER METHODS ///////////////////////////////

        /**
         * setOnProgressListener --
         * Set the onProgressListener class variable.
         * @param onProgressListener The listener to use when setting the class variable.
         */
        public final void setOnProgressListener(OnProgressListener<Progress> onProgressListener) {

            this.onProgressListener = onProgressListener;

        }

        /**
         * getOnProgressListener --
         * Get the value of the onProgressListener class variable.
         * @return The value of onProgressListener class variable.
         */
        public final OnProgressListener<Progress> getOnProgressListener() {

            return onProgressListener;

        }

        /////////////////////////////////// TASK ACTION METHODS ////////////////////////////////////

        /**
         * postProgress --
         * Calls onProgress method in both the class and onProgressListener class. Called on the
         * Main UI thread.
         * @param PROGRESS The "progress" this task has made. (Usually used as percentage)
         */
        public final void postProgress(final Progress PROGRESS) {

            // Run the onProgress methods on the Main UI thread
            MAIN_HANDLER.post(() -> {

                this.onProgress(PROGRESS);

                // If the onProgressListener has been set, then call its method as well
                if (onProgressListener != null)

                    onProgressListener.onProgress(PROGRESS);

            });

        }

        ////////////////////////////////// TASK LIFECYCLE METHODS //////////////////////////////////

        /**
         * onPreExecute --
         * Always called before doInBackground() method is executed.
         */
        protected void onPreExecute() { }

        /**
         * doInBackground --
         * The code/"task" to run on a background thread.
         * @return The "result" of the task that was executed.
         * @throws Exception If an error has occurred during execution of the task. If an exception
         * is thrown, it triggers the onTaskFailed method to be executed on the Main UI thread.
         */
        protected abstract Result doInBackground() throws Exception;

        /**
         * onPostExecute --
         * Called after doInBackground() method is executed, as long as the task has NOT failed.
         * @param result The "result" of the task that was executed.
         */
        protected void onPostExecute(Result result) { }

        /**
         * onTaskFailed --
         * Called if an Exception has been thrown from the doInBackground() method.
         * @param e The Exception thrown from the doInBackground() method.
         */
        protected void onTaskFailed(Exception e) { e.printStackTrace(); }

        /**
         * onProgress --
         * Called on Main UI thread after a call to the postProgress method has been called. Used to
         * update any UI about the progress of this task.
         * @param progress The "progress" this task has made. (Usually used as percentage)
         */
        protected void onProgress(Progress progress) { }

    }

}
