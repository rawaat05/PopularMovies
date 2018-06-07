package com.nomaa.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nomaa.popularmovies.data.MoviesContract.*;

/**
 * Created by nomaa on 8/24/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    public static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB HELPER HERE", "NOW WRITING");
        final String SQLITE_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME + " (" +
                MoviesContract.PopularEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.COLUMN_TITLE + " TEXT, " +
                MoviesContract.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.COLUMN_RATING + " TEXT, " +
                MoviesContract.COLUMN_POSTER + " TEXT);";

        final String SQLITE_CREATE_TOP_RATED_TABLE = "CREATE TABLE " + TopRatedEntry.TABLE_NAME + " (" +
                MoviesContract.TopRatedEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.COLUMN_TITLE + " TEXT, " +
                MoviesContract.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.COLUMN_RATING + " TEXT, " +
                MoviesContract.COLUMN_POSTER + " TEXT);";

        final String SQLITE_CREATE_FAVORITES_TABLE = "CREATE TABLE " + Favorites.TABLE_NAME + " (" +
                MoviesContract.Favorites._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.COLUMN_TITLE + " TEXT, " +
                MoviesContract.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.COLUMN_RATING + " TEXT, " +
                MoviesContract.COLUMN_POSTER + " TEXT);";

        db.execSQL(SQLITE_CREATE_POPULAR_TABLE);
        db.execSQL(SQLITE_CREATE_TOP_RATED_TABLE);
        db.execSQL(SQLITE_CREATE_FAVORITES_TABLE);
        Log.e("DB HELPER HERE", "DONE WRITING");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TopRatedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Favorites.TABLE_NAME);
        onCreate(db);
    }
}
