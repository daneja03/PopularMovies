package com.daneja.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by deepakaneja.cs on 1/22/2017.
 * Helper class to perform network related or other general tasks.
 */

public class MoviesNetworkUtility {

    private static final String LOG_TAG = MoviesNetworkUtility.class.getSimpleName();
    public static final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_MOVIES_PATH = "popular";
    public static final String TOP_RATED_MOVIES_PATH = "top_rated";
    public static final String API_KEY = "<Your Api Key>";

    /**
     * Gets the data for a single movie by its id embedded in the requestUrl
     * @param requestUrl
     * @return
     */
    public static Movie fetchMovieById(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error occured while making the request.", exception);
        }
        Movie movie = extraMovieDataFromJson(jsonResponse);
        return movie;
    }

    /**
     * Gets the popular or top rated movies from the movie db.
     * @param requestUrl
     * @return
     */
    public static List<Movie> fetchMoviesFromTheMoviesDb(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error occured while making the request.", exception);
        }

        List<Movie> movies = extractMoviesFromJson(jsonResponse);
        return movies;
    }

    /**
     * format the release date to be displayed on detail activity in MMM dd, yyyy format
     * @param releaseDate
     * @return
     */
    private static String formatDate(String releaseDate){
        String releaseDateValue = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat desiredFormatter = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date = formatter.parse(releaseDate);
            return desiredFormatter.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error in formatting date:", e);
        }
        return releaseDateValue;
    }

    /**
     * Prepare a comma separated string of genres from the JsonArray
     * @param name
     * @return
     * @throws JSONException
     */
    private static String getCommaSeparatedStringForJsonArray(JSONArray name) throws JSONException {
        if (name.length() > 0) {
            StringBuilder nameBuilder = new StringBuilder();

            for (int i = 0; i < name.length(); i++) {
                JSONObject genre = name.getJSONObject(i);
                String genreStr = genre.getString("name");
                nameBuilder.append(" " + genreStr).append(",");
            }

            nameBuilder.deleteCharAt(0);
            nameBuilder.deleteCharAt(nameBuilder.length() - 1);

            return nameBuilder.toString();
        } else {
            return "";
        }
    }

    /**
     * Extract the Movie object from the raw json resturned from the api call
     * @param jsonResponse
     * @return
     */
    private static Movie extraMovieDataFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        Movie movie = null;
        try {
            JSONObject movieObject = new JSONObject(jsonResponse);
            int id = movieObject.getInt("id");
            String posterPath = movieObject.getString("poster_path");
            String backdropPath = movieObject.getString("backdrop_path");
            JSONArray genresArray = movieObject.getJSONArray("genres");
            String genres = getCommaSeparatedStringForJsonArray(genresArray);
            String homepagePath = movieObject.getString("homepage");
            String title = movieObject.getString("title");
            String overview = movieObject.getString("overview");
            Double userRating = movieObject.getDouble("vote_average");
            int voteCount = movieObject.getInt("vote_count");
            String releaseDateString = movieObject.getString("release_date");
            String releaseDate = formatDate(releaseDateString);
            movie = new Movie(id, title, overview, posterPath, releaseDate, userRating, voteCount, backdropPath, genres, homepagePath);
        } catch (JSONException exception) {
            Log.e(LOG_TAG, "Unable to parse moves json response:", exception);
        }
        return movie;
    }

    /**
     * Extract the movies list object from the raw json returned from api call
     * @param jsonResponse
     * @return
     */
    private static List<Movie> extractMoviesFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray results = rootObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
                int id = movieObject.getInt("id");
                String posterPath = movieObject.getString("poster_path");
                Movie movie = new Movie(id, posterPath);
                movies.add(movie);
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, "Unable to parse moves json response:", exception);
        }
        return movies;
    }

    /**
     * Create URL object
     * @param stringUrl
     * @return
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Invalid URL", exception);
        }
        return url;
    }

    /**
     * Create HTTPUrlConnection object the get the response stream
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + httpURLConnection.getResponseCode());
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to open connection", exception);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Read from the stream returned as response
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }
}
