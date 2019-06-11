package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.models.Review;

import java.util.ArrayList;

public class AddReviewAdapter extends RecyclerView.Adapter<AddReviewViewHolder> {
    private Context context;
    private ArrayList<Review> addReviewArrayList;

    private String review;
    private String userId;
    private float rating;
    private Review addReview;

    public AddReviewAdapter(Context context, ArrayList<Review> addReviewArrayList) {
        this.context = context;
        this.addReviewArrayList = addReviewArrayList;
    }

    @NonNull
    @Override
    public AddReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AddReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.add_review_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddReviewViewHolder addReviewViewHolder, int i) {
        addReview = addReviewArrayList.get(i);
        review = addReview.getReview();
        userId = addReview.getUserId();
        rating = addReview.getRating();

        addReviewViewHolder.mTextViewReview.setText(review);
        addReviewViewHolder.mTextViewUser.setText(userId);
        addReviewViewHolder.mRatingBarReview.setRating(rating);

    }

    @Override
    public int getItemCount() {
        return addReviewArrayList.size();
    }
}

