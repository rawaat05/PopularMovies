package com.nomaa.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by nomaa on 8/24/2017.
 */

public class MoviesProvider extends ContentProvider {

    MoviesDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int POPULAR_MOVIES = 100;
    public static final int POPULAR_MOVIES_ID = 101;

    public static final int TOP_RATED_MOVIES = 200;
    public static final int TOP_RATED_MOVIES_MOVIES_ID = 201;

    public static final int FAVORITES_MOVIES = 300;
    public static final int FAVORITES_MOVIES_ID = 301;

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());

        return true;
    }

    static {
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_popular, POPULAR_MOVIES);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_popular + "/#", POPULAR_MOVIES_ID);

        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_top_rated, TOP_RATED_MOVIES);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_top_rated + "/#", TOP_RATED_MOVIES_MOVIES_ID);

        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_favorites, FAVORITES_MOVIES);
        sUriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.path_favorites + "/#", FAVORITES_MOVIES_ID);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        Log.e("Check uri", "" + sUriMatcher.match(uri));
        switch(sUriMatcher.match(uri)){
            case POPULAR_MOVIES:
                cursor = db.query(MoviesContract.PopularEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case POPULAR_MOVIES_ID:
                selection = MoviesContract.PopularEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MoviesContract.PopularEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TOP_RATED_MOVIES:
                cursor = db.query(MoviesContract.TopRatedEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TOP_RATED_MOVIES_MOVIES_ID:
                selection = MoviesContract.TopRatedEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MoviesContract.TopRatedEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVORITES_MOVIES:
                cursor = db.query(MoviesContract.Favorites.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVORITES_MOVIES_ID:
                selection = MoviesContract.Favorites._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MoviesContract.Favorites.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
            default:
//                throw new IllegalArgumentException("Uri could not be processed!");
                selection = MoviesContract.Favorites._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(MoviesContract.Favorites.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case POPULAR_MOVIES_ID:
                long id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, values);
                if(id == -1){
                    Log.e("MoviesProvider", "couldn't insert into database");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case TOP_RATED_MOVIES_MOVIES_ID:
                long id2 = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, values);
                if(id2 == -1){
                    Log.e("MoviesProvider", "couldn't insert into database");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id2);
            case FAVORITES_MOVIES_ID:
                long id3 = db.insert(MoviesContract.Favorites.TABLE_NAME, null, values);
                if(id3 == -1){
                    Log.e("MoviesProvider", "couldn't insert into database");
                    return null;
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id3);
            default:
                throw new IllegalArgumentException("wrong match in insert");
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {

            case POPULAR_MOVIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.PopularEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                        Log.e("BULK INSERT ID NUMBER: ", Long.toString(_id));
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            case TOP_RATED_MOVIES:
                db.beginTransaction();
                int rowsInserted2 = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesContract.TopRatedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted2++;
                        }
                        Log.e("BULK INSERT ID NUMBER: ", Long.toString(_id));
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted2 > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted2;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case POPULAR_MOVIES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(MoviesContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case POPULAR_MOVIES_ID:
                // Delete a single row given by the ID in the URI
                selection = MoviesContract.PopularEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MoviesContract.PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(MoviesContract.TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TOP_RATED_MOVIES_MOVIES_ID:
                // Delete a single row given by the ID in the URI
                selection = MoviesContract.PopularEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MoviesContract.TopRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITES_MOVIES_ID:
                // Delete a single row given by the ID in the URI
                selection = MoviesContract.Favorites._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(MoviesContract.Favorites.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
