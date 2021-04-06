package edu.ncc.nest.nestapp.AsynchronousTask;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public final class TaskExecutor {

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = TaskExecutor.class.getSimpleName();

    /** Whether or not this class should allow core thread time-out.
     * {@link ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)} */
    private static final boolean ALLOW_CORE_THREAD_TIME_OUT = false;

    /** Whether or not this class should pre-start all core threads on initialization.
     * {@link ThreadPoolExecutor#prestartAllCoreThreads()} */
    private static final boolean PRE_START_CORE_THREADS = true;

    /** The wrapped {@link ThreadPoolExecutor} to execute {@link BackgroundTask} on. */
    private final ExecutorService executorService;

    ///////////////////////////////////////// CONSTRUCTORS /////////////////////////////////////////

    /**
     * Creates a new TaskExecutor object and initializes the internal ExecutorService to a new
     * single thread executor.
     */
    public TaskExecutor() { this(1); }

    /**
     * Creates a new TaskExecutor object and initializes the internal ExecutorService to a new
     * fixed thread pool executor. {@link Executors#newFixedThreadPool(int)}
     *
     * @param nThreads The number of threads to keep in the pool, even if they are idle
     */
    public TaskExecutor(int nThreads) { this(nThreads, nThreads, 0L); }

    /**
     * Creates a new TaskExecutor object and initializes the internal ExecutorService to a new
     * customized ThreadPoolExecutor using the provided parameters.
     * {@link ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue,
     * ThreadFactory)}
     *
     * @param corePoolSize The number of threads to keep in the pool, even if they are idle,
     *                     unless allowCoreThreadTimeOut is set
     * @param maximumPoolSize The maximum number of threads to allow in the pool
     * @param keepAliveTime When the number of threads is greater than the core, this is the maximum
     *                      time that excess idle threads will wait for new tasks before terminating.
     *                      (TimeUnit.MILLISECONDS)
     * @throws IllegalArgumentException If coreThreadTimeOut is true and the current keep-alive
     *                                  time is not greater than zero
     */
    public TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {

        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), TaskThread::new);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;

        threadPoolExecutor.allowCoreThreadTimeOut(ALLOW_CORE_THREAD_TIME_OUT);

        if (PRE_START_CORE_THREADS)

            threadPoolExecutor.prestartAllCoreThreads();

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
     * @throws ExecutionException If the task threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the task was cancelled
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
     * @throws ExecutionException If the task threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the task was cancelled
     * @throws TimeoutException If the wait timed out
     */
    public <Result> void executeAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask,
                                        long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {

        backgroundTask.submitOn(executorService).get(timeout, unit);

    }

    /**
     *
     * @param backgroundTask
     * @param <Result>
     * @return
     * @throws ExecutionException If the task threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the task was cancelled
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
     * @throws ExecutionException If the task threw an exception
     * @throws InterruptedException If the current thread was interrupted while waiting
     * @throws CancellationException If the task was cancelled
     * @throws TimeoutException If the wait timed out
     */
    public <Result> Result submitAndWait(@NonNull final BackgroundTask<?, Result> backgroundTask,
                                         long timeout, TimeUnit unit)
            throws ExecutionException, InterruptedException, TimeoutException {

        return backgroundTask.submitOn(executorService).get(timeout, unit);

    }

    /////////////////////////////////////// WRAPPER METHODS ////////////////////////////////////////

    /**
     * Initiates an orderly shutdown in which previously submitted
     * tasks are executed, but no new tasks will be accepted.
     * Invocation has no additional effect if already shut down.
     *
     * <p>This method does not wait for previously submitted tasks to
     * complete execution.  Use {@link #awaitTermination awaitTermination}
     * to do that.
     *
     * <p><br>Wrapper method for: {@link ExecutorService#shutdown()}</p>
     */
    public void shutdown() { executorService.shutdown(); }

    /**
     * Attempts to stop all actively executing tasks, halts the
     * processing of waiting tasks, and returns a list of the tasks
     * that were awaiting execution.
     *
     * <p>This method does not wait for actively executing tasks to
     * terminate.  Use {@link #awaitTermination awaitTermination} to
     * do that.
     *
     * <p>There are no guarantees beyond best-effort attempts to stop
     * processing actively executing tasks.  For example, typical
     * implementations will cancel via {@link Thread#interrupt}, so any
     * task that fails to respond to interrupts may never terminate.
     *
     * <p><br>Wrapper method for: {@link ExecutorService#shutdownNow()}</p>
     *
     * @return {@link List} of tasks that never commenced execution
     */
    // TODO Possibly convert the returned List to a List of BackgroundTask
    public List<Runnable> shutdownNow() { return executorService.shutdownNow(); }

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * <p><br>Wrapper method for: {@link ExecutorService#awaitTermination(long, TimeUnit)}</p>
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     * @throws InterruptedException If interrupted while waiting
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {

        return executorService.awaitTermination(timeout, unit);

    }

    /**
     * Returns {@code true} if this executor has been shut down.
     *
     * <p><br>Wrapper method for: {@link ExecutorService#isShutdown()}</p>
     *
     * @return {@code true} if this executor has been shut down, false otherwise.
     */
    public boolean isShutdown() { return executorService.isShutdown(); }

    /**
     * Returns {@code true} if all tasks have completed following shut down.
     * Note that {@code isTerminated} is never {@code true} unless
     * either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * <p><br>Wrapper method for: {@link ExecutorService#isTerminated()}</p>
     *
     * @return {@code true} if all tasks have completed following shut down, {@code false} otherwise
     */
    public boolean isTerminated() { return executorService.isTerminated(); }

    /////////////////////////////////////// EXECUTION THREAD ///////////////////////////////////////

    /**
     * Represents a Thread that BackgroundTask will be run on.
     */
    private static class TaskThread extends Thread {

        /** The tag to use when printing to the log from this class. */
        private static final String LOG_TAG = TaskThread.class.getSimpleName();

        /** Keeps track of how many ExecutionThreads have been created */
        private static final AtomicInteger threadCount = new AtomicInteger(0);

        public TaskThread(Runnable runnable) {
            super(runnable, "TaskThread::" + threadCount.incrementAndGet());

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