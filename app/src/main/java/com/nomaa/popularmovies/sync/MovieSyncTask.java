package com.nomaa.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nomaa.popularmovies.data.MoviesContract;
import com.nomaa.popularmovies.utilities.NetworkUtils;
import com.nomaa.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;

/**
 * Created by nomaa on 8/26/2017.
 */

class MovieSyncTask {


    synchronized public static void syncMovies(Context context) {

        syncPopularMovies(context);
        syncTopRatedMovies(context);
    }

    private static void syncPopularMovies(Context context) {

        try {

            URL url = NetworkUtils.getPopularUrl(context);
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpURL(url);

            ContentValues[] movieValues =
                    OpenMovieJsonUtils.getMovieContentValuesFromJson(context, jsonMovieResponse);

            if(movieValues != null && movieValues.length != 0){

                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MoviesContract.PopularEntry.CONTENT_URI, null, null);

                int numInserted = contentResolver.bulkInsert(MoviesContract.PopularEntry.CONTENT_URI, movieValues);

                Log.e("SYNC POPULAR_MOVIES: ", " " + numInserted);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void syncTopRatedMovies(Context context) {

        try {

            URL url = NetworkUtils.getTopRatedUrl(context);
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpURL(url);

            ContentValues[] movieValues =
                    OpenMovieJsonUtils.getMovieContentValuesFromJson(context, jsonMovieResponse);

            if(movieValues != null && movieValues.length != 0){

                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MoviesContract.TopRatedEntry.CONTENT_URI, null, null);

                int numInserted = contentResolver.bulkInsert(MoviesContract.TopRatedEntry.CONTENT_URI, movieValues);

                Log.e("SYNC TOP_RATED_MOVIES: ", " " + numInserted);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
