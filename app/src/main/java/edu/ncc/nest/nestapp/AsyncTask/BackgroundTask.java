package edu.ncc.nest.nestapp.AsyncTask;

@SuppressWarnings("unused")
public abstract class BackgroundTask<Progress, Result> {

    private OnProgressListener<Progress> onProgressListener = null;

    ///////////////////////////////// ON PROGRESS LISTENER METHODS /////////////////////////////////

    public final void setOnProgressListener(OnProgressListener<Progress> onProgressListener) {

        this.onProgressListener = onProgressListener;

    }

    public final OnProgressListener<Progress> getOnProgressListener() {

        return onProgressListener;

    }

    ///////////////////////////////////// TASK ACTION METHODS //////////////////////////////////////

    public final void postProgress(final Progress PROGRESS) {



    }

    //////////////////////////////////// TASK LIFECYCLE METHODS ////////////////////////////////////

    protected void onPreExecute() {}

    protected abstract Result doInBackground() throws Exception;

    protected void onPostExecute(Result result) {}

    protected void onTaskFailed(Exception e) { e.printStackTrace(); }

    protected void onProgress(Progress progress) {}

}
