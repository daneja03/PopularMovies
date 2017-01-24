package com.daneja.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import static com.daneja.popularmovies.MoviesNetworkUtility.API_KEY;
import static com.daneja.popularmovies.MoviesNetworkUtility.POPULAR_MOVIES_PATH;
import static com.daneja.popularmovies.MoviesNetworkUtility.TMDB_BASE_URL;
import static com.daneja.popularmovies.MoviesNetworkUtility.TOP_RATED_MOVIES_PATH;

/**
 * Main activity of Popular Movies, displays a grid of movie posters.
 */
public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;

    MoviesAdapter moviesAdapter;
    GridView gridView;
    TextView txtEmptyView;
    ProgressBar progressBar;

    /**
     * onCreate method of the main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridview);
        txtEmptyView = (TextView) findViewById(R.id.txt_empty_grid);
        progressBar = (ProgressBar) findViewById(R.id.pgb_grid_loading);

        //Set the onItemClickListener for this gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                Movie clieckedMovie = moviesAdapter.getItem(position);
                intent.putExtra("id", clieckedMovie.getId());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data

        if (networkInfo != null && networkInfo.isConnected()) {
            MoviesNetworkingTask moviesNetworkingTask = new MoviesNetworkingTask();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String sortValue = preferences.getString(getString(R.string.value_field_sort_preference), getString(R.string.value_field_popularity));
            String url = "";
            if (sortValue == getString(R.string.value_field_popularity)) {
                url = TMDB_BASE_URL + POPULAR_MOVIES_PATH + "?api_key=" + API_KEY;
            } else {
                url = TMDB_BASE_URL + TOP_RATED_MOVIES_PATH + "?api_key=" + API_KEY;
            }
            moviesNetworkingTask.execute(url);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.pgb_grid_loading);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            txtEmptyView.setText(R.string.no_internet_connection);
        }
    }


    /**
     * Creating the settings menu on main activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * perform actions when settings menu option is clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.movie_settings) {
            Intent preferenceIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferenceIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This class is responsible of getting the json stream for popular
     * or top rated movies from the movie db.
     * Accepts a string url and returns list of movie posters
     */
    public class MoviesNetworkingTask extends AsyncTask<String, Void, List<Movie>> {
        /**
         * Load the movie posters asynchronously in background
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
         * @param movies
         */
        @Override
        protected void onPostExecute(List<Movie> movies) {

            progressBar.setVisibility(View.GONE);

            if (movies != null) {
                moviesAdapter = new MoviesAdapter(MainActivity.this, movies);

                gridView.setEmptyView(txtEmptyView);
                gridView.setAdapter(moviesAdapter);
            } else {
                Log.e(LOG_TAG, getString(R.string.no_movies_found));
                txtEmptyView.setText(getString(R.string.no_movies_found));
            }
        }
    }

}
