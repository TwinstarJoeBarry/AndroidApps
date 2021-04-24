package edu.ncc.nest.nestapp.async;

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

    /////////////////////////////////////// CLASS VARIABLES ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = BackgroundTask.class.getSimpleName();

    /** The {@link Handler} object used to post {@link Runnable} objects to the main thread */
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /** Represents whether or not this task has been interrupted by an Exception */
    private final AtomicBoolean bInterrupted = new AtomicBoolean(false);

    /** Represents whether or not the {@code futureTask}'s call method has ever been invoked */
    private final AtomicBoolean bInvoked = new AtomicBoolean(false);

    /** The {@link FutureTask} object that represents this task */
    private final FutureTask<Result> futureTask;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    // TODO Possibly add a String (tag) to identify this task
    public BackgroundTask() {

        futureTask = new FutureTask<>(() -> {

            if (bInvoked.getAndSet(true))

                throw new IllegalStateException("Task has already been invoked");

            /* TODO Possibly add Thread.setPriority here to allow each task to define its
             *   own priority
             */

            Result result = null;

            try {

                // Execute the background code, and get the "Result"
                result = doInBackground();

            } catch (final Throwable e) {

                bInterrupted.set(true);

                synchronized(mainHandler) {

                    // Call the onError method on the main thread.
                    mainHandler.post(() -> onError(e));

                }

                // This should allow the FutureTask to see the Exception
                throw e;

            } finally {

                // Ensures that any pending object references have been released
                Binder.flushPendingCommands();

                // If the task was not interrupted
                if (!bInterrupted.get()) {

                    // Store the result as final so we can use it in our posted run-ables
                    final Result finalResult = result;

                    synchronized (mainHandler) {

                        // Call the onPostExecute method with the result on the main thread
                        mainHandler.post(() -> onPostExecute(finalResult));

                    }

                }

            }

            return result;

        });

    }

    /////////////////////////////////////// CLASS METHODS //////////////////////////////////////////

    /**
     * Returns whether or not this task has been interrupted due to an Exception.
     *
     * @return Whether or not this task has been interrupted due to an Exception
     */
    public final boolean getInterrupted() { return bInterrupted.get(); }

    /**
     * Returns whether or not the {@code futureTask}'s call method has ever been invoked.
     *
     * @return Whether or not the {@code futureTask}'s call method has ever been invoked
     */
    public final boolean getInvoked() { return bInvoked.get(); }

    /**
     * Executes this task using the provided Executor.
     *
     * @param executor The {@link Executor} to execute this task on
     * @throws IllegalStateException If this task has already been invoked
     */
    public final synchronized void executeOn(@NonNull Executor executor) {

        if (bInvoked.get())

            throw new IllegalStateException("Task has already been executed.");

        executor.execute(futureTask);

    }

    /**
     * Executes this task using the provided Executor, and then returns a {@link Future} object that
     * represents this task.
     *
     * @param executor The {@link Executor} to execute this task on
     * @return A {@link Future} object that represents this task
     */
    public final synchronized Future<Result> submitOn(@NonNull Executor executor) {

        executeOn(executor);

        return futureTask;

    }

    /**
     * Calls onProgressUpdate method in the onProgressListener class.
     * (Useful for loading bars, etc...)
     *
     * @param progress The "progress" this task has made.
     */
    protected final void postProgress(final Progress progress) {

        synchronized (mainHandler) {

            // Run the onProgressUpdate method on the main/UI thread
            mainHandler.post(() -> onProgressUpdate(progress));

        }

    }

    //////////////////////////////////// TASK LIFECYCLE METHODS ////////////////////////////////////

    /**
     * The code/task to run in the background.
     *
     * @return The "result" of the task that was executed
     * @throws Exception If an error has occurred during execution of the task. If a Exception
     * is thrown, it triggers the {@link #onError} method to be executed on the Main
     * UI thread
     */
    @WorkerThread
    protected abstract Result doInBackground() throws Exception;

    /**
     * Called when any progress is posted by this task.
     *
     * @param progress The "progress" of the task.
     */
    @MainThread
    protected void onProgressUpdate(Progress progress) { }

    /**
     * Called after {@link #doInBackground} method is executed, as long as the task has NOT failed.
     *
     * @param result The "result" of the task that was executed
     */
    @MainThread
    protected void onPostExecute(Result result) { }

    /**
     * Called if an exception is thrown during the execution of {@link #doInBackground()}.
     *
     * @param throwable The {@link Throwable} thrown during execution
     */
    @MainThread
    protected void onError(@NonNull Throwable throwable) {

        // Print the Throwable's stack trace as an error to the log
        Log.e(LOG_TAG, Log.getStackTraceString(throwable));

    }

}