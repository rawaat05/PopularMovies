package com.nomaa.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by nomaa on 8/26/2017.
 */

public class MovieFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchMovieTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mFetchMovieTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Context context = getApplicationContext();
                MovieSyncTask.syncMovies(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        };

        mFetchMovieTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters job) {
        if(mFetchMovieTask != null)
            mFetchMovieTask.cancel(true);

        return true;
    }
}
