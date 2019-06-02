package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class ResultsIDBViewHolder extends RecyclerView.ViewHolder  {
    public TextView mTextViewSnippet;
    public TextView mTextViewSource;
    public RatingBar mRatingBarReview;

    public ResultsIDBViewHolder(@NonNull View itemView) {
        super(itemView);
        initView(itemView);

    }

    public void initView(View itemView) {
        mTextViewSnippet = (TextView) itemView.findViewById(R.id.textViewSnippet);
        mTextViewSource = (TextView) itemView.findViewById(R.id.textViewSource);
        mRatingBarReview = (RatingBar) itemView.findViewById(R.id.ratingBar2);
    }

}
