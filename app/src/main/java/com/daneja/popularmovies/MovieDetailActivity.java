package com.daneja.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.daneja.popularmovies.MoviesNetworkUtility.API_KEY;
import static com.daneja.popularmovies.MoviesNetworkUtility.TMDB_BASE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    TextView txtEmptyView;
    ProgressBar progressBar;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        txtEmptyView = (TextView) findViewById(R.id.txt_empty_view);
        progressBar = (ProgressBar) findViewById(R.id.pgb_movie_detail);

    }


    @Override
    protected void onStart() {
        super.onStart();

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            txtEmptyView.setText("");
            Intent intent = getIntent();
            int id = intent.getIntExtra("id", 0);
            String tmdbMovieDetailUrl = TMDB_BASE_URL + id + "?api_key=" + API_KEY;
            MovieDetailAsyncTask movieDetailAsyncTask = new MovieDetailAsyncTask();
            movieDetailAsyncTask.execute(tmdbMovieDetailUrl);
        }
        else {
            View contentView = findViewById(R.id.detail_content);
            contentView.setVisibility(View.GONE);
            txtEmptyView.setText(getString(R.string.no_internet_connection));
            progressBar.setVisibility(View.GONE);
            Log.e(LOG_TAG, "No network");
        }
    }

    public class MovieDetailAsyncTask extends AsyncTask<String, Void, Movie> {
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

            progressBar.setVisibility(View.GONE);

            if (movie != null) {
                ImageView movieThumbnail = (ImageView) findViewById(R.id.img_movie_thumbnail);
                Picasso.with(MovieDetailActivity.this)
                        .load(movie.getThumbnailImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(movieThumbnail);

                ImageView movieBackdropImage = (ImageView) findViewById(R.id.img_movie_backdrop);
                Picasso.with(MovieDetailActivity.this)
                        .load(movie.getBackdropPath())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(movieBackdropImage);

                TextView movieTitle = (TextView) findViewById(R.id.txt_movie_title);
                movieTitle.setText(movie.getTitle());

                TextView movieGenres = (TextView) findViewById(R.id.txt_genres);
                movieGenres.setText(movie.getGenres());

                TextView movieVotesHeading = (TextView) findViewById(R.id.txt_votes_heading);
                movieVotesHeading.setText(getString(R.string.votes_heading));

                TextView movieRatingHeading = (TextView) findViewById(R.id.txt_rating_heading);
                movieRatingHeading.setText(getString(R.string.rating_heading));

                TextView movieVotes = (TextView) findViewById(R.id.txt_votes);
                movieVotes.setText(movie.getVoteCount() + "");

                TextView movieVotesAverage = (TextView) findViewById(R.id.txt_rating);
                movieVotesAverage.setText(movie.getVoteAverage() + "");

                TextView movieOverviewHeading = (TextView) findViewById(R.id.txt_overview_heading);
                movieOverviewHeading.setText(getString(R.string.synopsis_heading));

                TextView movieOverview = (TextView) findViewById(R.id.txt_overview);
                movieOverview.setText(movie.getOverview());

                TextView movieReleaseDate = (TextView) findViewById(R.id.txt_release_date);
                movieReleaseDate.setText(getString(R.string.prefix_released) + movie.getReleaseDate());

            }
            else {
                txtEmptyView.setText(getString(R.string.no_movies_found));
            }
        }
    }


}
