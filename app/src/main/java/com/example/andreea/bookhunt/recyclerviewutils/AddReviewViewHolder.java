package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class AddReviewViewHolder extends RecyclerView.ViewHolder  {
    public TextView mTextViewReview;
    public TextView mTextViewUser;
    public RatingBar mRatingBarReview;

    public AddReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        initView(itemView);

    }

    public void initView(View itemView) {
        mTextViewReview = (TextView) itemView.findViewById(R.id.textViewReview);
        mTextViewUser = (TextView) itemView.findViewById(R.id.textViewUser);
        mRatingBarReview = (RatingBar) itemView.findViewById(R.id.ratingBarReview);
    }

}
