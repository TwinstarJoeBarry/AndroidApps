package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public final class TaskExecutor {

    public static final String LOG_TAG = TaskExecutor.class.getSimpleName();

    private final ExecutorService executorService;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    public TaskExecutor() {

        executorService = Executors.newSingleThreadExecutor(ExecutionThread::new);

    }

    public TaskExecutor(int nThreads) { this(nThreads, nThreads, 0L); }

    public TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {

        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(), ExecutionThread::new);

        if (maximumPoolSize > corePoolSize)

            ((ThreadPoolExecutor) executorService).allowCoreThreadTimeOut(true);

        ((ThreadPoolExecutor) executorService).prestartAllCoreThreads();

    }

    //////////////////////////////////////// CLASS METHODS /////////////////////////////////////////

    /**
     *
     * @param backgroundTask
     * @param <Result>
     * @throws java.util.concurrent.RejectedExecutionException If this task cannot be accepted for
     * execution
     */
    public <Result> void execute(@NonNull final BackgroundTask<?, Result> backgroundTask) {

        backgroundTask.executeOn(executorService);

    }

    /**
     *
     * @param backgroundTask
     * @param <Result>
     * @return
     * @throws java.util.concurrent.RejectedExecutionException If this task cannot be accepted for
     * execution
     */
    public <Result> Future<Result> submit(@NonNull final BackgroundTask<?, Result> backgroundTask) {

        return backgroundTask.submitOn(executorService);

    }

    /**
     *
     * @param backgroundTask
     * @param <Result>
     * @throws ExecutionException If the computation threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the computation was cancelled
     */
    public <Result> void executeAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask)
            throws ExecutionException, InterruptedException {

        backgroundTask.submitOn(executorService).get();

    }

    /**
     *
     * @param backgroundTask
     * @param timeout
     * @param unit
     * @param <Result>
     * @throws ExecutionException If the computation threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the computation was cancelled
     * @throws TimeoutException If the wait timed out
     */
    public <Result> void executeAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask, long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {

        backgroundTask.submitOn(executorService).get(timeout, unit);

    }

    /**
     *
     * @param backgroundTask
     * @param <Result>
     * @return
     * @throws ExecutionException If the computation threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the computation was cancelled
     */
    public <Result> Result submitAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask)
            throws ExecutionException, InterruptedException {

        return backgroundTask.submitOn(executorService).get();

    }

    /**
     *
     * @param backgroundTask
     * @param timeout
     * @param unit
     * @param <Result>
     * @return
     * @throws ExecutionException If the computation threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the computation was cancelled
     * @throws TimeoutException If the wait timed out
     */
    public <Result> Result submitAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask, long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {

        return backgroundTask.submitOn(executorService).get(timeout, unit);

    }

    @Override
    protected void finalize() throws Throwable {

        executorService.shutdown();

        super.finalize();

    }

    /////////////////////////////////////// EXECUTION THREAD ///////////////////////////////////////

    /**
     * ExecutionThread: Represents a Thread that BackgroundTask will be run on.
     */
    private static class ExecutionThread extends Thread {

        private static final String LOG_TAG = ExecutionThread.class.getSimpleName();

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

}