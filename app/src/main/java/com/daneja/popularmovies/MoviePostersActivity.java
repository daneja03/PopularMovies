package com.daneja.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daneja.popularmovies.MoviesNetworkUtility.API_KEY;
import static com.daneja.popularmovies.MoviesNetworkUtility.POPULAR_MOVIES_PATH;
import static com.daneja.popularmovies.MoviesNetworkUtility.TMDB_BASE_URL;
import static com.daneja.popularmovies.MoviesNetworkUtility.TOP_RATED_MOVIES_PATH;

/**
 * Main activity of Popular Movies, displays a grid of movie posters.
 */
public class MoviePostersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private final String LOG_TAG = MoviePostersActivity.class.getSimpleName();

    private static final int MOVIES_LOADER_ID = 1;

    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    MoviePostersAdapter moviesAdapter;
    LoaderManager loaderManager;
    SharedPreferences preferences;
    String currentPreferenceValue;
    String tmdbUrl;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_empty_grid)
    TextView txtEmptyView;
    @BindView(R.id.pgb_grid_loading)
    ProgressBar loadingIndicator;

    /**
     * onCreate method of the main activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(this, 300);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));

        moviesAdapter = new MoviePostersAdapter(this, new ArrayList<Movie>());
        recyclerView.setAdapter(moviesAdapter);


        loaderManager = getLoaderManager();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, initialize loader

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(MOVIES_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            txtEmptyView.setText(R.string.no_internet_connection);
        }
    }


    /**
     * Creating the settings menu on main activity
     *
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
     *
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



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.value_field_sort_preference))){
            moviesAdapter.clear();

            txtEmptyView.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);

            getLoaderManager().restartLoader(MOVIES_LOADER_ID, null,this);
        }
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        tmdbUrl = "";
        currentPreferenceValue = preferences.getString(getString(R.string.value_field_sort_preference), getString(R.string.value_field_popularity));
        if (currentPreferenceValue == getString(R.string.value_field_user_rating)) {
            tmdbUrl = TMDB_BASE_URL + TOP_RATED_MOVIES_PATH + "?api_key=" + API_KEY;
        } else {
            tmdbUrl = TMDB_BASE_URL + POPULAR_MOVIES_PATH + "?api_key=" + API_KEY;
        }
        MoviesLoader moviesLoader = new MoviesLoader(this, tmdbUrl);
        return moviesLoader;
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param movies The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        moviesAdapter.clear();

        if (movies != null) {
            moviesAdapter.addMovies(movies);
            loadingIndicator.setVisibility(View.GONE);
        } else {
            Log.e(LOG_TAG, getString(R.string.no_movies_found));
            txtEmptyView.setText(getString(R.string.no_movies_found));
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        moviesAdapter.clear();
    }


}
