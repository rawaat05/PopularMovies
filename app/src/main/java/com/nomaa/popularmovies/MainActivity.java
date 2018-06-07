package com.nomaa.popularmovies;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.nomaa.popularmovies.data.MoviesContract;
import com.nomaa.popularmovies.sync.MovieSyncUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener{

    public static final String[] projection = {
            MoviesContract.COLUMN_TITLE,
            MoviesContract.COLUMN_OVERVIEW,
            MoviesContract.COLUMN_RATING,
            MoviesContract.COLUMN_RELEASE_DATE,
            MoviesContract.COLUMN_POSTER,
            MoviesContract.PopularEntry._ID};

    public static final int INDEX_MOVIE_TITLE = 0;
    public static final int INDEX_MOVIE_OVERVIEW = 1;
    public static final int INDEX_MOVIE_RATING = 2;
    public static final int INDEX_MOVIE_RELEASEDATE = 3;
    public static final int INDEX_MOVIE_POSTERPATH = 4;
    public static final int INDEX_MOVIE_ID = 5;

    public static final int MOVIE_LOADER_ID = 162;

    public static int mSpinnerSelection = 0;

    private MovieAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private GridLayoutManager layoutManager;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        layoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

        MovieSyncUtils.initialize(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_entries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.e("Position ms", "" + (int) mPosition);
//        mRecyclerView.scrollToPosition(mPosition);

        if(mSpinnerSelection == 2){
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mSpinnerSelection == 0) {
            return new CursorLoader(
                    this, MoviesContract.PopularEntry.CONTENT_URI, projection, null, null, null);
        }
        if (mSpinnerSelection == 1){
            return new CursorLoader(
                    this, MoviesContract.TopRatedEntry.CONTENT_URI, projection, null, null, null);
        }
        if (mSpinnerSelection == 2){
            return new CursorLoader(
                    this, MoviesContract.Favorites.CONTENT_URI, projection, null, null, null);
        }
        else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);
//        if(mPosition == RecyclerView.NO_POSITION)
//            mPosition = 0;
        mRecyclerView.scrollToPosition(-1);
        // if(data.getCount() != 0) showMovieDataView();
        Log.e("ON LOAD FINISHED: ", " " + data.getCount());

        if(data.getCount() > 0) {
            mLoadingIndicator = findViewById(R.id.loading_spinner);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerSelection = position;
        Log.e("Spinner position: ", "" + position);

        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

}
