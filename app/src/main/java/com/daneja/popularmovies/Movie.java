package com.daneja.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by deepakaneja.cs on 1/22/2017.
 * Represents a movie fetched from the movie database
 */

public class Movie implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private double voteAverage;
    private String releaseDate;
    private int voteCount;
    private String backdropPath;

    private String imageBaseUrl = "http://image.tmdb.org/t/p/";

    /**
     * Constructor used by movie detail activity
     *
     * @param id
     * @param title
     * @param overview
     * @param posterPath
     * @param releaseDate
     * @param voteAverage
     * @param voteCount
     * @param backdropPath
     */
    public Movie(int id, String title, String overview, String posterPath, String releaseDate, double voteAverage, int voteCount, String backdropPath) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.backdropPath = backdropPath;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(backdropPath);
    }

    public Movie(Parcel parcel) {
        id = parcel.readInt();
        posterPath = parcel.readString();
        title = parcel.readString();
        overview = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readDouble();
        voteCount = parcel.readInt();
        backdropPath = parcel.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public String getBackdropPath() {
        return imageBaseUrl + "w500/" + backdropPath;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getThumbnailImageUrl() {
        return imageBaseUrl + "w185/" + getPosterPath();
    }

    public String getMoviePosterUrl() {
        return imageBaseUrl + "w342/" + getPosterPath();
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
}
