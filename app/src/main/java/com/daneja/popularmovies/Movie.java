package com.daneja.popularmovies;

/**
 * Created by deepakaneja.cs on 1/22/2017.
 * Represents a movie fetched from the movie database
 */

public class Movie {
    private int id;
    private String title;
    private String overview;
    private String posterPath;
    private double voteAverage;
    private String releaseDate;
    private int voteCount;
    private String backdropPath;
    private String genres;
    private String homepagePath;

    private String imageBaseUrl = "http://image.tmdb.org/t/p/";

    /**
     * Constructor used by main activity to initialize each movie poster
     * @param id
     * @param posterPath
     */
    public Movie(int id, String posterPath){
        this.id = id;
        this.posterPath = posterPath;
    }

    /**
     * Constructor used by movie detail activity
     * @param id
     * @param title
     * @param overview
     * @param posterPath
     * @param releaseDate
     * @param voteAverage
     * @param voteCount
     * @param backdropPath
     * @param genres
     * @param homepagePath
     */
    public Movie(int id, String title, String overview, String posterPath, String releaseDate, double voteAverage, int voteCount, String backdropPath, String genres, String homepagePath) {
        this(id, posterPath);
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.backdropPath = backdropPath;
        this.genres = genres;
        this.homepagePath = homepagePath;
    }

    public String getBackdropPath() {
        return imageBaseUrl + "w500/" + backdropPath;
    }

    public String getGenres() {
        return genres;
    }

    public String getHomepagePath() {
        return homepagePath;
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
