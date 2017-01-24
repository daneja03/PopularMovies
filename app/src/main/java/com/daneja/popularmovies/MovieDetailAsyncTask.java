package com.daneja.popularmovies;

import android.os.AsyncTask;

/**
 * Created by deepakaneja.cs on 1/25/2017.
 */

public class MovieDetailAsyncTask extends AsyncTask<String, Void, Movie> {
    MovieDetailOnTaskCompleted listener;

    public MovieDetailAsyncTask(MovieDetailOnTaskCompleted listener){
        this.listener = listener;
    }

    @Override
    protected Movie doInBackground(String... params) {
        if (params == null) {
            return null;
        }

        Movie movie = MoviesNetworkUtility.fetchMovieById(params[0]);
        return movie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        listener.onTaskCompleted(movie);
    }
}

