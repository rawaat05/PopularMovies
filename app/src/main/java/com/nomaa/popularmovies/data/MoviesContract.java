package com.nomaa.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nomaa on 8/24/2017.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.nomaa.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String path_popular = "popular";
    public static final String path_top_rated = "top_rated";
    public static final String path_favorites = "favorites";


    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_RELEASE_DATE = "releaseDate";

    public static final String COLUMN_OVERVIEW = "overview";

    public static final String COLUMN_RATING = "rating";

    public static final String COLUMN_POSTER = "poster_path";

    public static final String _ID = "id";

    public static final String AUTHOR = "author";

    public static final String CONTENT = "content";

    private MoviesContract(){};

    public static final class PopularEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path_popular).build();

        public static final String TABLE_NAME = "popular";

    }

    public static final class TopRatedEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path_top_rated).build();

        public static final String TABLE_NAME = "top_rated";

    }

    public static final class Favorites implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(path_favorites).build();

        public static final String TABLE_NAME = "favorites";

    }
}
