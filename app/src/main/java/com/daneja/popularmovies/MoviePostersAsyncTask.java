package com.daneja.popularmovies;

import android.os.AsyncTask;

import java.util.List;

/**
 * This class is responsible of getting the json stream for popular
 * or top rated movies from the movie db.
 * Accepts a string url and returns list of movie posters
 */
public class MoviePostersAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    private MoviePostersOnTaskCompleted listener;

    public MoviePostersAsyncTask(MoviePostersOnTaskCompleted listener){
        this.listener = listener;
    }

    /**
     * Load the movie posters asynchronously in background
     *
     * @param url of the popular or top rated movies
     * @return list of movie posters
     */
    @Override
    protected List<Movie> doInBackground(String... params) {
        if (params == null) {
            return null;
        }
        List<Movie> movies = MoviesNetworkUtility.fetchMoviesFromTheMoviesDb(params[0]);
        return movies;
    }

    /**
     * Sets up the gridview and binds it with the movieadapter
     *
     * @param movies
     */
    @Override
    protected void onPostExecute(List<Movie> movies) {
        listener.onTaskCompleted(movies);

    }
}

