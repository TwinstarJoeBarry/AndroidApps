package edu.ncc.nest.nestapp.BackgroundTask;

import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.CancellationException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BackgroundTask<Result> {

    private final String LOG_TAG = BackgroundTask.class.getSimpleName();

    private final AtomicBoolean taskInvoked = new AtomicBoolean(false);

    private final FutureTask<Result> futureTask;

    public BackgroundTask() {

        futureTask = new FutureTask<>(() -> {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Result result = null;

            try {

                if (taskInvoked.getAndSet(true))

                    throw new RuntimeException("Task has already been invoked.");


            } catch (CancellationException e) {

                Log.w(LOG_TAG, "BackgroundTask was cancelled.");


            } catch (Throwable throwable) {

                Log.e(LOG_TAG, "Error executing BackgroundTask.");

                throw throwable;

            } finally {

                Binder.flushPendingCommands();

                final Result finalResult = result;


            }

            return result;

        });

    }

}
