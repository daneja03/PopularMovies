package com.daneja.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deepakaneja.cs on 1/25/2017.
 */

public class MoviePostersAdapter extends RecyclerView.Adapter<MoviePostersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_movie_thumbnail) ImageView moviePoster;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    private List<Movie> movies;
    private Context context;

    public MoviePostersAdapter(Context context, List<Movie> movies){
        this.movies = movies;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public MoviePostersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View moviePosterView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(moviePosterView);
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MoviePostersAdapter.ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        Picasso.with(context)
                .load(movie.getMoviePosterUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.moviePoster);

        holder.moviePoster.setOnClickListener(new View.OnClickListener(){
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                context.startActivity(intent);
            }
        });
    }

    /**
     * Clear the movies list.
     */
    public void clear() {
        int size = movies.size();
        movies.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * Adding movies to the movies collection
     * @param movies
     */
    public void addMovies(List<Movie> movies){
        this.movies.addAll(movies);
        notifyItemRangeInserted(0, this.movies.size());
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return movies.size();
    }
}
