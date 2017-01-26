package com.daneja.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by deepakaneja.cs on 1/26/2017.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    String url;

    public MoviesLoader(Context context, String url){
        super(context);
        this.url = url;
    }
    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * @return The result of the load operation.
     * @throws OperationCanceledException if the load is canceled during execution.
     * @see #isLoadInBackgroundCanceled
     * @see #cancelLoadInBackground
     * @see #onCanceled
     */
    @Override
    public List<Movie> loadInBackground() {
        if (url == null) {
            return null;
        }
        List<Movie> movies = MoviesNetworkUtility.fetchMoviesFromTheMoviesDb(url);
        return movies;
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
