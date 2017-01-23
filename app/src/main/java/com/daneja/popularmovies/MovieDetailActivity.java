package com.daneja.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.daneja.popularmovies.MoviesNetworkUtility.API_KEY;
import static com.daneja.popularmovies.MoviesNetworkUtility.TMDB_BASE_URL;

public class MovieDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String tmdbMovieDetailUrl = TMDB_BASE_URL + id + "?api_key=" + API_KEY;
        MovieDetailAsyncTask movieDetailAsyncTask = new MovieDetailAsyncTask();
        movieDetailAsyncTask.execute(tmdbMovieDetailUrl);
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

                TextView movieVotes = (TextView) findViewById(R.id.txt_votes);
                movieVotes.setText(movie.getVoteCount() + "");

                TextView movieVotesAverage = (TextView) findViewById(R.id.txt_rating);
                movieVotesAverage.setText(movie.getVoteAverage() + "");

                TextView movieOverview = (TextView) findViewById(R.id.txt_overview);
                movieOverview.setText(movie.getOverview());

                TextView movieReleaseDate = (TextView) findViewById(R.id.txt_release_date);
                movieReleaseDate.setText(getString(R.string.prefix_released) + movie.getReleaseDate());
            }
        }
    }


}
