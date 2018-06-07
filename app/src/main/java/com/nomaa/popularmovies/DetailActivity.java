package com.nomaa.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nomaa.popularmovies.data.MoviesContract;
import com.nomaa.popularmovies.data.MoviesDbHelper;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.datatype.Duration;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri uriWithId;

    private TextView title;
    private TextView rating;
    private TextView releaseDate;
    private TextView overview;
    private ImageView poster;
    private ImageView favoriteIndicator;

    private static final int MOVIE_LOADER_ID = 262;

    public static final String[] projection = {
            MoviesContract.COLUMN_TITLE,
            MoviesContract.COLUMN_OVERVIEW,
            MoviesContract.COLUMN_RATING,
            MoviesContract.COLUMN_RELEASE_DATE,
            MoviesContract.COLUMN_POSTER,
            MoviesContract.PopularEntry._ID};

    private static int id;
    private static String movieName;
    private static String posterPath;
    private static String release;

    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_OVERVIEW = 1;
    public static final int INDEX_MOVIE_RATING = 2;
    public static final int INDEX_MOVIE_RELEASEDATE = 3;
    public static final int INDEX_MOVIE_POSTERPATH = 4;
    public static final int INDEX_MOVIE_ID = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uriWithId = getIntent().getData();

        Log.e("uriCheck" , uriWithId.toString());

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    this, uriWithId, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToPosition(0);

        if(data.getCount() != 0) {

            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();

            title = (TextView) findViewById(R.id.title);
            rating = (TextView) findViewById(R.id.rating);
            releaseDate = (TextView) findViewById(R.id.release_date);
            overview = (TextView) findViewById(R.id.overview);
            poster = (ImageView) findViewById(R.id.poster);

            title.setText(data.getString(INDEX_MOVIE_TITLE));
            rating.setText(data.getString(INDEX_MOVIE_RATING));
            Log.e("Release", data.getString(INDEX_MOVIE_RELEASEDATE));
            releaseDate.setText(months[Integer.parseInt(data.getString(INDEX_MOVIE_RELEASEDATE).substring(5, 6))]);
            overview.setText(data.getString(INDEX_MOVIE_OVERVIEW));
            Picasso.with(this).load(data.getString(INDEX_MOVIE_POSTERPATH)).into(poster);

            //Save data for addRemoveFavorite method
            id = data.getInt(INDEX_MOVIE_ID);
            movieName = data.getString(INDEX_MOVIE_TITLE);
            posterPath = data.getString(INDEX_MOVIE_POSTERPATH);
            release = data.getString(INDEX_MOVIE_RELEASEDATE);

            releaseDate.append(" " + data.getString(INDEX_MOVIE_RELEASEDATE).substring(8) + ", "
                    + data.getString(INDEX_MOVIE_RELEASEDATE).substring(0,4));

            title.append(" (" + data.getString(INDEX_MOVIE_RELEASEDATE).substring(0,4) + ")");

            MoviesDbHelper dbHelper = new MoviesDbHelper(getBaseContext());

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursorForHeart = db.rawQuery("SELECT COUNT(*) FROM favorites " +
                    "WHERE _id = " +  id  + " AND title = '" + movieName + "'", null);

            cursorForHeart.moveToPosition(0);

            if(cursorForHeart.getInt(0) == 1) {
                favoriteIndicator = (ImageView) findViewById(R.id.add_remove_favorite);
                favoriteIndicator.setImageResource(R.drawable.heart);
            }

            cursorForHeart.close();
        }
        else{
            Log.e("DetailActivity: ", "data is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void addRemoveFavorite(View view) {

        favoriteIndicator = (ImageView) findViewById(R.id.add_remove_favorite);

        ContentValues movieValue = new ContentValues(5);
        movieValue.put(MoviesContract.COLUMN_TITLE, movieName);
        movieValue.put(MoviesContract.COLUMN_RATING, rating.getText().toString());
        movieValue.put(MoviesContract.COLUMN_RELEASE_DATE, release);
        movieValue.put(MoviesContract.COLUMN_OVERVIEW, overview.getText().toString());
        movieValue.put(MoviesContract.COLUMN_POSTER, posterPath);
        movieValue.put(MoviesContract.Favorites._ID, id);

        MoviesDbHelper dbHelper = new MoviesDbHelper(getBaseContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

//        Cursor checkCursor = db.rawQuery("SELECT * FROM " + MoviesContract.Favorites.TABLE_NAME
//                + " WHERE title = '" + movieName + "'", null);
        Cursor checkCursor = db.query(MoviesContract.Favorites.TABLE_NAME, null, "title = ?",
                new String[] {movieName}, null, null, null);

        checkCursor.moveToPosition(0);

        Log.e("CHECK", "" + (checkCursor == null? true : false) + " : " + checkCursor.getCount());
        if((checkCursor == null) || (checkCursor.getCount() == 0)){
            db.insert(MoviesContract.Favorites.TABLE_NAME, null, movieValue);
            Toast.makeText(getBaseContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            favoriteIndicator.setImageResource(R.drawable.heart);
        } else {
            db.delete(MoviesContract.Favorites.TABLE_NAME, "title = ?", new String[] {movieName});
//            db.rawQuery("DELETE FROM favorites WHERE title = " + movieName, null);
            Toast.makeText(getBaseContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
            favoriteIndicator.setImageResource(R.drawable.unheart);
        }

        checkCursor.close();

    }

    public void openReviews(View view) {

        Intent reviewsIntent = new Intent(getBaseContext(), ReviewsActivity.class);

        reviewsIntent.putExtra("movieId", id);

        startActivity(reviewsIntent);
    }
}
