package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.models.Review;
import com.example.andreea.bookhunt.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddReviewAdapter extends RecyclerView.Adapter<AddReviewViewHolder> {
    private Context context;
    private ArrayList<Review> addReviewArrayList;

    private String review;
    private String userId;
    private float rating;
    private Review addReview;
    private String reviewId;
    private AddReviewViewHolder addReviewViewHolder1;


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
        reviewId = addReview.getReviewId();
        addReviewViewHolder1 = addReviewViewHolder;
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByKey().equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren() || dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String username = ds.getValue(User.class).getUsername();
                        // addReviewViewHolder1.mTextViewReview.setText(review);
                        addReviewViewHolder1.mTextViewUser.setText(username);
                        //  addReviewViewHolder1.mRatingBarReview.setRating(rating);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addReviewViewHolder1.mTextViewReview.setText(review);
//        addReviewViewHolder.mTextViewUser.setText(userId);
        addReviewViewHolder1.mRatingBarReview.setRating(rating);

    }

    @Override
    public int getItemCount() {
        return addReviewArrayList.size();
    }
}

