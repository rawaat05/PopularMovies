package com.nomaa.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.nomaa.popularmovies.MainActivity;
import com.nomaa.popularmovies.data.MoviesContract;

import java.util.concurrent.TimeUnit;

/**
 * Created by nomaa on 8/26/2017.
 */

public class MovieSyncUtils {

    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 2;

    private static boolean sInitialized;

    private static final String MOVIE_SYNC_TAG = "movie-sync";

    static void scheduleFirebaseJobDispatcherSync(Context context){

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job movieSyncJob = dispatcher.newJobBuilder()
                .setService(MovieFirebaseJobService.class)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTag(MOVIE_SYNC_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(movieSyncJob);

    }

    synchronized public static void initialize(final Context context) {

        if(sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Uri popularUri = MoviesContract.PopularEntry.CONTENT_URI;
                Uri topRatedUri = MoviesContract.TopRatedEntry.CONTENT_URI;

                String[] popularProjectionColumns = {MoviesContract.PopularEntry._ID};
                String[] topRatedProjectionColumns = {MoviesContract.TopRatedEntry._ID};

                Cursor popularCursor = context.getContentResolver().query(
                        popularUri,
                        popularProjectionColumns,
                        null,
                        null,
                        null);
                Cursor topRatedCursor = context.getContentResolver().query(
                        topRatedUri,
                        topRatedProjectionColumns,
                        null,
                        null,
                        null);

                if(popularCursor == null || popularCursor.getCount() == 0 ||
                        topRatedCursor == null || topRatedCursor.getCount() == 0)
                    startImmediateSync(context);

                popularCursor.close();
                topRatedCursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(Context context){
        Intent intentToSyncImmediately = new Intent(context, PopularMoviesSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
