package com.nomaa.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomaa.popularmovies.data.MoviesContract;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder>{

    final private Context mContext;

    private ContentValues[] contentValues;

    ReviewsAdapter(Context mContext, ContentValues[] values) {
        this.mContext = mContext;

        contentValues = values;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_reviews, parent, false);

        view.setFocusable(true);

        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {

        holder.tvAuthor.setText(contentValues[position].getAsString(MoviesContract.AUTHOR));
        holder.tvContent.setText(contentValues[position].getAsString(MoviesContract.CONTENT));
    }

    @Override
    public int getItemCount() {

        if(contentValues == null)
            return 0;

        return contentValues.length;
    }

    class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder{

        final TextView tvAuthor;
        final TextView tvContent;
        View mView;


        ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;

            this.tvAuthor = mView.findViewById(R.id.author);
            this.tvContent = mView.findViewById(R.id.content);
        }


    }
}
