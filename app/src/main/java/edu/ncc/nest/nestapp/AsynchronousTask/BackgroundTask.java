package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BackgroundTask<Progress, Result> {

    /**
     * Mainly used to listen for any "progress" updates from a {@link BackgroundTask} object. Can
     * also be used for other purposes.
     * @param <Progress> The data type that will represent the "progress" of a task.
     */
    public interface OnProgressListener<Progress> {

        /**
         * Called by a task, to inform the listener of any progress the related task has made.
         * @param progress The "progress" of the related task.
         */
        @MainThread
        void onProgressUpdate(Progress progress);

    }

    /////////////////////////////////////// CLASS VARIABLES ////////////////////////////////////////

    public static final String LOG_TAG = BackgroundTask.class.getSimpleName();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final AtomicBoolean bInterrupted = new AtomicBoolean(false);

    private final AtomicBoolean bInvoked = new AtomicBoolean(false);

    private volatile OnProgressListener<Progress> onProgressListener;

    private final FutureTask<Result> futureTask;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    // TODO Possibly add a String (name) to identify this task
    public BackgroundTask() {

        futureTask = new FutureTask<>(() -> {

            // TODO Possibly move Process.setThreadPriority here to allow each task to define its own priority

            Result result = null;

            try {

                // Execute the background code, and get the "Result"
                result = doInBackground();

            } catch (Throwable e) {

                // Call the onError method on the main thread.
                mainHandler.post(() -> onError(e));

                bInterrupted.set(true);

                // Throw the throwable since we aren't handling the error here
                // This should allow the FutureTask and Thread to see the Exception
                throw e;

            } finally {

                // Ensures that any pending object references have been released
                Binder.flushPendingCommands();

                // If the task was not interrupted
                if (!bInterrupted.get()) {

                    // Store the result as final so we can use it in our posted run-ables
                    final Result finalResult = result;

                    // Call the onPostExecute method with the result on the main thread
                    mainHandler.post(() -> onPostExecute(finalResult));

                }

            }

            return result;

        });

    }

    /////////////////////////////////////// CLASS METHODS //////////////////////////////////////////

    /**
     * Set the onProgressListener class variable.
     *
     * @param onProgressListener The listener to use when setting the class variable.
     */
    public final void setOnProgressListener(OnProgressListener<Progress> onProgressListener) {

        this.onProgressListener = onProgressListener;

    }

    /**
     * Get the value of the onProgressListener class variable.
     *
     * @return The value of onProgressListener class variable.
     */
    public final OnProgressListener<Progress> getOnProgressListener() {

        return onProgressListener;

    }

    /**
     * Calls onProgressUpdate method in the onProgressListener class.
     * (Useful for loading bars, etc...)
     *
     * @param progress The "progress" this task has made.
     */
    protected final void postProgress(@NonNull final Progress progress) {

        // If the onProgressListener has been set, then call its method as well
        if (onProgressListener != null)

            // Run the onProgress methods on the Main UI thread
            mainHandler.post(() -> onProgressListener.onProgressUpdate(progress));

    }

    public final boolean getInterrupted() { return bInterrupted.get(); }

    public final boolean getInvoked() { return bInvoked.get(); }

    public boolean isCancelled() { return futureTask.isCancelled(); }

    public final synchronized void executeOn(@NonNull Executor executor) {

        // If we are NOT on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread");

        if (this.getInvoked())

            throw new IllegalStateException("Task has already been executed.");

        this.onPreExecute();

        executor.execute(futureTask);

    }

    public final synchronized Future<Result> submitOn(@NonNull Executor executor) {

        executeOn(executor);

        return futureTask;

    }

    //////////////////////////////////// TASK LIFECYCLE METHODS ////////////////////////////////////

    /**
     * Always called before task is executed.
     */
    @MainThread
    protected void onPreExecute() { }

    /**
     * The code/task to run on a background thread.
     *
     * @return The "result" of the task that was executed
     * @throws Exception If an error has occurred during execution of the task. If a Exception
     * is thrown, it triggers the {@link #onError} method to be executed on the Main
     * UI thread
     */
    @WorkerThread
    protected abstract Result doInBackground() throws Exception;

    /**
     * Called after {@link #doInBackground} method is executed, as long as the task has NOT failed.
     *
     * @param result The "result" of the task that was executed
     */
    @MainThread
    protected void onPostExecute(Result result) { }

    /**
     * Called if an exception is thrown during execution.
     *
     * @param throwable The {@link Throwable} thrown during execution
     */
    @MainThread
    protected void onError(@NonNull Throwable throwable) {

        Log.e(LOG_TAG, throwable.getClass().getCanonicalName() + ": " +
                throwable.getMessage());

    }

}