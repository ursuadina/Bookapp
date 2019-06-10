package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.models.Review;
import com.example.andreea.bookhunt.recyclerviewutils.AddReviewAdapter;
import com.example.andreea.bookhunt.recyclerviewutils.ResultsIDBAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private RatingBar ratingBar;
    private Review mReview;
    private ArrayList<Review> reviewArrayList;
    private AddReviewAdapter reviewAdapter;
    private RecyclerView recyclerViewReviews;
    private Button buttonAddReviewBH;
    private EditText editTextReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();

        initView();
    }

    private void initView() {
        textViewTitle = findViewById(R.id.textViewReview);
        ratingBar = findViewById(R.id.ratingBarAddReview);
        buttonAddReviewBH = findViewById(R.id.buttonAddReviewBH);
        editTextReview = findViewById(R.id.editTextReview);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.rvAddReview);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        reviewArrayList =getReview();
        reviewAdapter = new AddReviewAdapter(ReviewActivity.this, reviewArrayList);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    public ArrayList<Review> getReview() {
        //String mPhotoUrl, String mTitle, String mCountry, double mPrice, double mRating, String typeTrip, String startDate, String endDate
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(new Review("this book surprised me", "uadina123", (float) 3.5));
        reviews.add(new Review("is a very good book","ioana",4));
        reviews.add(new Review("awesome", "andreea123", 3));
        return reviews;
    }

    public void btnAddReviewBHOnClick(View view) {
        String reviewAdded = editTextReview.getText().toString();
        float rating = ratingBar.getRating();
        reviewArrayList.add(new Review(reviewAdded, "andreea123",rating));
    }
}
