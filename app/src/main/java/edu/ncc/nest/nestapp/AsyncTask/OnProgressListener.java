package edu.ncc.nest.nestapp.AsyncTask;

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
    void onProgressUpdate(Progress progress);

}
