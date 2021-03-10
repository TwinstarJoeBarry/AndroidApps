package edu.ncc.nest.nestapp.AsyncTask;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class TaskExecutor {

    private static final ExecutorService READ_EXECUTOR_SERVICE =
            new ThreadPoolExecutor(4, 16,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static final ExecutorService WRITE_EXECUTOR_SERVICE =
            Executors.newSingleThreadExecutor();

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    

}
