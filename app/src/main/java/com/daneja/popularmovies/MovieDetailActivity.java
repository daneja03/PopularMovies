package com.daneja.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.daneja.popularmovies.MoviesNetworkUtility.API_KEY;
import static com.daneja.popularmovies.MoviesNetworkUtility.TMDB_BASE_URL;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailOnTaskCompleted {

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @BindView(R.id.txt_empty_view) TextView txtEmptyView;
    @BindView(R.id.pgb_movie_detail) ProgressBar progressBar;
    @BindView(R.id.img_movie_thumbnail) ImageView movieThumbnail;
    @BindView(R.id.detail_content) View contentView;
    @BindView(R.id.img_movie_backdrop) ImageView movieBackdropImage;
    @BindView(R.id.txt_movie_title) TextView movieTitle;
    @BindView(R.id.txt_genres) TextView movieGenres;
    @BindView(R.id.txt_votes_heading) TextView movieVotesHeading;
    @BindView(R.id.txt_rating_heading) TextView movieRatingHeading;
    @BindView(R.id.txt_votes) TextView movieVotes;
    @BindView(R.id.txt_rating) TextView movieVotesAverage;
    @BindView(R.id.txt_overview_heading) TextView movieOverviewHeading;
    @BindView(R.id.txt_overview) TextView movieOverview;
    @BindView(R.id.txt_release_date) TextView movieReleaseDate;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
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
            MovieDetailAsyncTask movieDetailAsyncTask = new MovieDetailAsyncTask(this);
            movieDetailAsyncTask.execute(tmdbMovieDetailUrl);
        }
        else {
            contentView.setVisibility(View.GONE);
            txtEmptyView.setText(getString(R.string.no_internet_connection));
            progressBar.setVisibility(View.GONE);
            Log.e(LOG_TAG, "No network");
        }
    }

    @Override
    public void onTaskCompleted(Movie movie) {

        progressBar.setVisibility(View.GONE);

        if (movie != null) {

            Picasso.with(MovieDetailActivity.this)
                    .load(movie.getThumbnailImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(movieThumbnail);

            Picasso.with(MovieDetailActivity.this)
                    .load(movie.getBackdropPath())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(movieBackdropImage);

            movieTitle.setText(movie.getTitle());
            movieGenres.setText(movie.getGenres());
            movieVotesHeading.setText(getString(R.string.votes_heading));
            movieRatingHeading.setText(getString(R.string.rating_heading));
            movieVotes.setText(movie.getVoteCount() + "");
            movieVotesAverage.setText(movie.getVoteAverage() + "");
            movieOverviewHeading.setText(getString(R.string.synopsis_heading));
            movieOverview.setText(movie.getOverview());
            movieReleaseDate.setText(getString(R.string.prefix_released) + movie.getReleaseDate());
        }
        else {
            txtEmptyView.setText(getString(R.string.no_movies_found));
        }

    }

}
