package com.nomaa.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.nomaa.popularmovies.utilities.NetworkUtils;
import com.nomaa.popularmovies.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class ReviewsActivity extends AppCompatActivity {

    private static int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        movieId = getIntent().getIntExtra("movieId", 0);

        new ReviewsTask(new WeakReference<Context>(this)).execute();
    }

    private static class ReviewsTask extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> context;

        ReviewsTask(WeakReference<Context> context) {
            super();

            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url = NetworkUtils.getReviewsUrl(movieId);

            if (url == null || "".equals(url.toString())) {
                return null;//if url is null, return
            }

            String operationResultString = "";

            try {
                operationResultString = NetworkUtils.getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return operationResultString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            try {
                ContentValues[] values = OpenMovieJsonUtils.getReviewsContentValuesFromJson(jsonString);

                if(values.length != 0) {
                    ProgressBar bar = ((Activity) context.get()).findViewById(R.id.loading_spinner);

                    bar.setVisibility(View.INVISIBLE);
                }

                RecyclerView recyclerView = ((Activity) context.get()).findViewById(R.id.recyclerview_reviews);

                recyclerView.setLayoutManager(new LinearLayoutManager(context.get()));

                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(context.get(), values);

                recyclerView.setAdapter(reviewsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
