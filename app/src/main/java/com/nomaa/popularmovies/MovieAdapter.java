package com.nomaa.popularmovies;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nomaa.popularmovies.data.MoviesContract;
import com.squareup.picasso.Picasso;


/**
 * Created by nomaa on 8/25/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    final private Context mContext;

    private Cursor mCursor;


    public MovieAdapter(Context context){

        Log.e("HEY CONSTRUCTOR HERE: ", "INITIALIZING MCONTEXT");
        mContext = context;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_movies, parent, false);


        view.setFocusable(true);
        Log.e("HEY ADAPTER HERE: ", "IN ON CREATE VIEW HOLDER");
        return new MovieAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, final int position) {

        mCursor.moveToPosition(position);

        String title = mCursor.getString(MainActivity.INDEX_MOVIE_TITLE);
        String rating = mCursor.getString(MainActivity.INDEX_MOVIE_RATING);
        String releaseDate = mCursor.getString(MainActivity.INDEX_MOVIE_RELEASEDATE);
        String overview = mCursor.getString(MainActivity.INDEX_MOVIE_OVERVIEW);
        String imagePath = mCursor.getString(MainActivity.INDEX_MOVIE_POSTERPATH);
        final int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);

//        holder.title.setText(title);
//        holder.rating.setText(rating);
//        holder.releaseDate.setText(releaseDate);
//        holder.overview.setText(overview);
        Picasso.with(holder.itemView.getContext()).load(imagePath).into(holder.poster);


        Log.e("HEY ADAPTER HERE: ", title + " " + rating + " " + releaseDate + " " + overview);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Position: ", "" + position);
                Intent detailIntent = new Intent(holder.itemView.getContext(), DetailActivity.class);

                if(MainActivity.mSpinnerSelection == 0){
                    Uri uriWithID = ContentUris.withAppendedId(MoviesContract.PopularEntry.CONTENT_URI, id);
                    detailIntent.setData(uriWithID);
                }
                else if(MainActivity.mSpinnerSelection == 1){
                    Uri uriWithID = ContentUris.withAppendedId(MoviesContract.TopRatedEntry.CONTENT_URI, id);
                    detailIntent.setData(uriWithID);
                } else {
                    Uri uriWithID = ContentUris.withAppendedId(MoviesContract.Favorites.CONTENT_URI, id);
                    detailIntent.setData(uriWithID);
                }


                holder.itemView.getContext().startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {

        if(mCursor == null)
            return 0;

        return mCursor.getCount();
    }

    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();

        Log.e("SWAP CURSOR: ", "I'M HERE");
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

//        final TextView title;
//        final TextView rating;
//        final TextView releaseDate;
//        final TextView overview;
        final ImageView poster;
        public View mView;


        public MovieAdapterViewHolder(View itemView) {

            super(itemView);
            
            mView = itemView;

//            title = (TextView) itemView.findViewById(R.id.title);
//            rating = (TextView) itemView.findViewById(R.id.rating);
//            releaseDate = (TextView) itemView.findViewById(R.id.release_date);
//            overview = (TextView) itemView.findViewById(R.id.overview);
            poster = (ImageView) itemView.findViewById(R.id.poster);

            Log.e("HEY VIEW HOLDER HERE: ", "SETTING DATA");
        }
    }
}
