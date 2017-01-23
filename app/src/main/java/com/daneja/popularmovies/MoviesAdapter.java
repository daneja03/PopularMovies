package com.daneja.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by deepakaneja.cs on 1/22/2017.
 * Custom ArrayAdapter for the Movie
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {

    Context context;

    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View movieGridItemView = convertView;
        if (movieGridItemView == null) {
            movieGridItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Movie currentMovieItem = getItem(position);

        ImageView imgMovieThumbnail = (ImageView) movieGridItemView.findViewById(R.id.img_movie_thumbnail);

        Picasso.with(context)
                .load(currentMovieItem.getMoviePosterUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imgMovieThumbnail);

        return movieGridItemView;
    }
}