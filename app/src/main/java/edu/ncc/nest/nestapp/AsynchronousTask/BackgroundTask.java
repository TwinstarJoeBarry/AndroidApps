package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BackgroundTask<Progress, Result> {

    /**
     * OnProgressListener --
     * Mainly used to listen for any "progress" updates from a TaskExecutor.BackgroundTask object. Can
     * also be used for other purposes.
     * @param <Progress> The data type that will represent the "progress" of a task.
     */
    public interface OnProgressListener<Progress> {

        /**
         * onProgress --
         * Called by a task, to inform the listener of any progress the related task has made.
         * @param progress The "progress" of the related task.
         */
        @MainThread
        void onProgressUpdate(Progress progress);

    }

    /////////////////////////////////////// CLASS VARIABLES ////////////////////////////////////////

    public static final String LOG_TAG = BackgroundTask.class.getSimpleName();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final AtomicBoolean bInvoked = new AtomicBoolean(false);

    private OnProgressListener<Progress> onProgressListener;

    private final FutureTask<Result> futureTask;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    // TODO Possibly add a String (name) to identify this task, add a priority (int) variable
    public BackgroundTask() {

        futureTask = new FutureTask<>(() -> {

            // TODO Possibly move Process.setThreadPriority here to allow each task to define its own priority

            boolean interrupted = true;

            Result result = null;

            try {

                // Make sure the task has not been previously executed
                if (bInvoked.getAndSet(true))

                    throw new RuntimeException("Task has already been invoked.");

                // Execute the background code, and get the "Result"
                result = doInBackground();

                interrupted = false;

            } catch (CancellationException cancellationException) {

                Log.w(LOG_TAG, "BackgroundTask was cancelled: " + cancellationException.getMessage());

                // Call the onCancelled method on the main thread.
                mainHandler.post(this::onCancelled);

            } catch (Throwable throwable) {

                Log.e(LOG_TAG, "An error occurred while executing BackgroundTask: " + throwable.getMessage());

                // Call the onError method on the main thread.
                mainHandler.post(() -> this.onError(throwable));

                // Throw the throwable since we aren't handling the error here
                // This allows the FutureTask to see the Exception
                throw throwable;

            } finally {

                // Ensures that any pending object references have been released
                Binder.flushPendingCommands();

                // If the task was not interrupted
                if (!interrupted) {

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
     * setOnProgressListener --
     * Set the onProgressListener class variable.
     *
     * @param onProgressListener The listener to use when setting the class variable.
     */
    public final void setOnProgressListener(OnProgressListener<Progress> onProgressListener) {

        this.onProgressListener = onProgressListener;

    }

    /**
     * getOnProgressListener --
     * Get the value of the onProgressListener class variable.
     *
     * @return The value of onProgressListener class variable.
     */
    public final OnProgressListener<Progress> getOnProgressListener() {

        return onProgressListener;

    }

    /**
     * postProgress --
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

    public final boolean getInvoked() { return bInvoked.get(); }

    public final boolean isCancelled() { return futureTask.isCancelled(); }

    public final boolean cancel(boolean mayInterruptIfRunning) {

        if (futureTask.cancel(mayInterruptIfRunning)) {

            if (!bInvoked.get())

                mainHandler.post(this::onCancelled);

            return true;

        } else

            return false;

    }

    public final synchronized void executeOn(@NonNull Executor executor) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        if (!bInvoked.get())

            throw new RuntimeException("Task has already been executed.");

        this.onPreExecute();

        executor.execute(futureTask);

    }

    public final synchronized FutureTask<Result> submitOn(@NonNull Executor executor) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        if (!bInvoked.get())

            throw new RuntimeException("Task has already been executed.");

        this.onPreExecute();

        executor.execute(futureTask);

        return futureTask;

    }

    //////////////////////////////////// TASK LIFECYCLE METHODS ////////////////////////////////////

    /**
     * onPreExecute --
     * Always called before doInBackground() method is executed.
     */
    @MainThread
    protected void onPreExecute() { }

    /**
     * doInBackground --
     * The code/task to run on a background thread.
     * @return The "result" of the task that was executed.
     * @throws Exception If an error has occurred during execution of the task. If a Execution
     * is thrown, it triggers the onCancelled method to be executed on the Main UI thread, with
     * a "result" value of null.
     */
    @WorkerThread
    protected abstract Result doInBackground() throws Exception;

    /**
     * onPostExecute --
     * Called after doInBackground() method is executed, as long as the task has NOT failed.
     * @param result The "result" of the task that was executed.
     */
    @MainThread
    protected void onPostExecute(Result result) { }

    @MainThread
    protected void onCancelled() { Log.w(LOG_TAG, "Task cancelled"); }

    @MainThread
    protected void onError(@NonNull Throwable throwable) { }

}
