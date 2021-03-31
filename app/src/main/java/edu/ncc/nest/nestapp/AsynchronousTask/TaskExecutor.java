package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public final class TaskExecutor {

    public static final String LOG_TAG = TaskExecutor.class.getSimpleName();

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final ThreadPoolExecutor threadPoolExecutor;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    public TaskExecutor() { this(4, 128, 1000L); }

    public TaskExecutor(int nThreads) { this(nThreads, nThreads, 0L); }

    public TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {

        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new SynchronousQueue<>(), ExecutionThread::new);

        if (maximumPoolSize > corePoolSize)

            threadPoolExecutor.allowCoreThreadTimeOut(true);

        threadPoolExecutor.prestartAllCoreThreads();

    }

    //////////////////////////////////////// CLASS METHODS /////////////////////////////////////////

    public <Result> void execute(@NonNull final ExecutableTask<?, Result> executableTask) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        if (executableTask.invoked())

            throw new RuntimeException("Task has already been executed.");

        if (executableTask.isCancelled())

            throw new RuntimeException("Task has been pre-cancelled.");

        executableTask.onPreExecute();

        executableTask.executeOn(threadPoolExecutor);

    }

    public <Result> Future<Result> submit(@NonNull final ExecutableTask<?, Result> executableTask) {

        // If we are NOT currently on the main Thread
        if (!Looper.getMainLooper().isCurrentThread())

            throw new RuntimeException("This method must be called from the main Thread.");

        if (executableTask.invoked())

            throw new RuntimeException("Task has already been executed.");

        if (executableTask.isCancelled())

            throw new RuntimeException("Task has been pre-cancelled.");

        executableTask.onPreExecute();

        return executableTask.submitOn(threadPoolExecutor);

    }

    public <Result> Result executeAndWait(@NonNull final ExecutableTask<?, Result> executableTask)
            throws ExecutionException, InterruptedException {

        return submit(executableTask).get();

    }

    public <Result> Result executeAndWait(@NonNull final ExecutableTask<?, Result> executableTask, long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {

        return submit(executableTask).get(timeout, unit);

    }

    @Override
    protected void finalize() throws Throwable {

        threadPoolExecutor.shutdown();

        super.finalize();

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

            /* Reset the thread priority to act as a background thread, so that it will have less
             * chance of impacting the responsiveness of the user interface.
             * Changing this affects how fast the tasks execute. */
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        }

    }

}