package edu.ncc.nest.nestapp.AsyncTask;

@SuppressWarnings("unused")
public abstract class BackgroundTask<Progress, Result> {

    private OnProgressListener<Progress> onProgressListener = null;

    public final void setOnProgressListener(OnProgressListener<Progress> onProgressListener) {

        this.onProgressListener = onProgressListener;

    }

    public final OnProgressListener<Progress> getOnProgressListener() {

        return onProgressListener;

    }

}
