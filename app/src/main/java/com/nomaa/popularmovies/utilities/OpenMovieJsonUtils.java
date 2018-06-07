package com.nomaa.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.nomaa.popularmovies.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nomaa on 8/26/2017.
 */

public class OpenMovieJsonUtils {

    private static final String BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/w500";


    public static ContentValues[] getMovieContentValuesFromJson
            (Context context, String jsonMovieResponse) throws JSONException {

        JSONObject base = new JSONObject(jsonMovieResponse);

        JSONArray resultsArray = base.getJSONArray("results");

        ContentValues[] movieValues = new ContentValues[resultsArray.length()];

        for(int i=0; i<resultsArray.length(); i++){

            JSONObject movie = resultsArray.getJSONObject(i);

            int id = movie.getInt("id");
            String title = movie.getString("title");
            String releaseDate = movie.getString("release_date");
            String overview = movie.getString("overview");
            String rating = movie.getString("vote_average");
            String imagePath = BASE_IMAGE_PATH + movie.getString("poster_path");

            ContentValues movieValue = new ContentValues(4);
            movieValue.put(MoviesContract.COLUMN_TITLE, title);
            movieValue.put(MoviesContract.COLUMN_RELEASE_DATE, releaseDate);
            movieValue.put(MoviesContract.COLUMN_OVERVIEW, overview);
            movieValue.put(MoviesContract.COLUMN_RATING, rating);
            movieValue.put(MoviesContract.COLUMN_POSTER, imagePath);
            movieValue.put(MoviesContract.PopularEntry._ID, id);

            movieValues[i] = movieValue;
        }

        return movieValues;
    }

    public static ContentValues[] getReviewsContentValuesFromJson
            (String jsonMovieResponse) throws JSONException {

        JSONObject base = new JSONObject(jsonMovieResponse);

        JSONArray resultsArray = base.getJSONArray("results");

        ContentValues[] movieValues = new ContentValues[resultsArray.length()];

        for(int i=0; i<resultsArray.length(); i++){

            JSONObject movie = resultsArray.getJSONObject(i);

            String author = movie.getString("author");
            String content = movie.getString("content");

            ContentValues movieValue = new ContentValues(2);
            movieValue.put(MoviesContract.AUTHOR, author);
            movieValue.put(MoviesContract.CONTENT, content);

            movieValues[i] = movieValue;
        }

        return movieValues;
    }
}
