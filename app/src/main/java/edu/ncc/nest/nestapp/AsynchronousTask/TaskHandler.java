package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public final class TaskHandler {

    public static final String LOG_TAG = TaskHandler.class.getSimpleName();

    private final ThreadPoolExecutor threadPoolExecutor;

    private final Handler mainHandler;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    public TaskHandler() { this(4, 128, 1000L); }

    public TaskHandler(int nThreads) { this(nThreads, nThreads, 0L); }

    public TaskHandler(int corePoolSize, int maximumPoolSize, long keepAliveTime) {

        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new SynchronousQueue<>(), ExecutionThread::new);

        if (maximumPoolSize > corePoolSize)

            threadPoolExecutor.allowCoreThreadTimeOut(true);

        threadPoolExecutor.prestartAllCoreThreads();

        mainHandler = new Handler(Looper.getMainLooper());

    }

    //////////////////////////////////////// CLASS METHODS /////////////////////////////////////////

    private <Result> FutureTask<Result> createFutureTask(@NonNull final ExecutableTask<?, Result> executableTask) {

        // Create a FutureTask for the executableTask and return it
        return (FutureTask<Result>) (executableTask.future = new FutureTask<>(() -> {

            Result result = null;

            try {

                // TODO Possibly move Process.setThreadPriority here to allow each task to define its own priority

                // Execute the background code, and get the "RESULT"
                result = executableTask.doInBackground();

            } catch (Throwable throwable) {

                // Set the task as cancelled
                executableTask.setCancelled(true);

                if (!(throwable instanceof CancellationException)) {

                    // Log a warning message saying that an error occurred during execution
                    Log.e(LOG_TAG, "An error occurred while executing task: " + throwable.getCause());

                    // Throw the throwable since we aren't handling the error here
                    // This allows the UncaughtExceptionHandler to handle the error
                    throw throwable;

                } else {

                    Log.w(LOG_TAG, "ExecutableTask was cancelled: " + throwable.getCause());

                    // Call the onCancelled method with the result on the main thread.
                    mainHandler.post(executableTask::onCancelled);

                }

            } finally {

                // Ensures that any pending object references have been released
                Binder.flushPendingCommands();

                // Store the result as final so we can use it in our posted run-ables
                final Result finalResult = result;

                // If the task was not cancelled
                if (!executableTask.isCancelled())

                    // Call the onPostExecute method with the result on the main thread
                    mainHandler.post(() -> executableTask.onPostExecute(finalResult));

            }

            return result;

        }));

    }

    public <Result> void execute(@NonNull final ExecutableTask<?, Result> executableTask) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        // TODO Add a check here to make sure the task is running or has been run

        // Make sure we create a FutureTask for the executable, in-case cancel is called in onPreExecute()
        RunnableFuture<Result> futureTask = createFutureTask(executableTask);

        executableTask.onPreExecute();

        threadPoolExecutor.execute(futureTask);

    }

    public <Result> Future<Result> submit(@NonNull final ExecutableTask<?, Result> executableTask) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        // TODO Add a check here to make sure the task is running or has been run

        // Make sure we create a FutureTask for the executable, in-case cancel is called in onPreExecute()
        RunnableFuture<Result> futureTask = createFutureTask(executableTask);

        executableTask.onPreExecute();

        threadPoolExecutor.execute(futureTask);

        return futureTask;

    }

    ////////////////////////////////////// EXECUTION THREAD ////////////////////////////////////////

    private static class ExecutionThread extends Thread {

        private static final AtomicInteger threadCount = new AtomicInteger(0);

        public ExecutionThread(Runnable runnable) {
            super(runnable, "ExecutionThread::" + threadCount.incrementAndGet());

            Log.d(LOG_TAG, "Thread created: " + getName());

        }

        @Override
        public void run() {

            /* Set the thread priority to act as a background thread, so that it will have less
             * chance of impacting the responsiveness of the user interface.
             * Changing this affects how fast the tasks execute. */
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            super.run();

        }

    }

    ////////////////////////////////////// EXECUTABLE TASK /////////////////////////////////////////

    public static abstract class ExecutableTask<Progress, Result> {

        public static final String LOG_TAG = ExecutableTask.class.getSimpleName();

        private final AtomicBoolean cancelled = new AtomicBoolean(false);

        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        private OnProgressListener<Progress> onProgressListener;

        private Future<Result> future;

        /////////////////////////////// ON PROGRESS LISTENER METHODS ///////////////////////////////

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

        /////////////////////////////////// TASK ACTION METHODS ////////////////////////////////////

        /**
         * postProgress --
         * Calls onProgress method in both the class and onProgressListener class. Called on the
         * Main UI thread.
         *
         * @param progress The "progress" this task has made. (Usually used as percentage)
         */
        public final void publishProgress(@NonNull final Progress progress) {

            // Run the onProgress methods on the Main UI thread
            mainHandler.post(() -> {

                this.onProgressUpdate(progress);

                // If the onProgressListener has been set, then call its method as well
                if (onProgressListener != null)

                    onProgressListener.onProgressUpdate(progress);

            });

        }

        private void setCancelled(boolean cancelled) { this.cancelled.set(cancelled); }

        public final boolean isCancelled() { return cancelled.get(); }

        public boolean cancel(boolean mayInterruptIfRunning) {

            if (future != null && future.cancel(mayInterruptIfRunning)) {

                cancelled.set(true);

                return true;

            } else

                return false;

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
         * @throws Exception If an error has occurred during execution of the task. If a Execution
         * is thrown, it triggers the onCancelled method to be executed on the Main UI thread, with
         * a "result" value of null.
         */
        protected abstract Result doInBackground() throws Exception;

        /**
         * onPostExecute --
         * Called after doInBackground() method is executed, as long as the task has NOT failed.
         * @param result The "result" of the task that was executed.
         */
        protected void onPostExecute(Result result) { }

        /**
         * onProgress --
         * Called on Main UI thread after a call to the postProgress method has been called. Used to
         * update any UI about the progress of this task.
         * @param progress The "progress" this task has made. (Usually used as percentage)
         */
        protected void onProgressUpdate(@NonNull Progress progress) { }

        protected void onCancelled() {

            Log.w(ExecutableTask.LOG_TAG, "Task cancelled");

        }

    }

}
