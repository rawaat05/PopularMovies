package com.nomaa.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nomaa on 8/26/2017.
 */

public class NetworkUtils {

    private static final String POPULAR_MOVIES_URL =
            "https://api.themoviedb.org/3/discover/movie?api_key=7b1aa406331bc855c7a2e8a15f086a95";

    private static final String TOP_RATED_MOVIES_URL =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=7b1aa406331bc855c7a2e8a15f086a95";

    private static String BASE_REVIEWS_URL =
            "https://api.themoviedb.org/3/movie/";


    public static URL getPopularUrl(Context context) {

        Uri movieUri = Uri.parse(POPULAR_MOVIES_URL).buildUpon().build();

        try {
            URL movieURL = new URL(movieUri.toString());
            return movieURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getTopRatedUrl(Context context) {

        Uri movieUri = Uri.parse(TOP_RATED_MOVIES_URL).buildUpon().build();

        try {
            URL movieURL = new URL(movieUri.toString());
            return movieURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getReviewsUrl(int movieId) {

        String finaUri = BASE_REVIEWS_URL + movieId + "/reviews?api_key=7b1aa406331bc855c7a2e8a15f086a95";

        Uri movieUri = Uri.parse(finaUri).buildUpon().build();

        try {
            URL movieURL = new URL(movieUri.toString());
            return movieURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpURL(URL url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {

            InputStream in = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();

            return response;

        } finally {
            httpURLConnection.disconnect();
        }
    }
}
