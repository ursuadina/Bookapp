package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.models.Review;
import com.example.andreea.bookhunt.recyclerviewutils.AddReviewAdapter;
import com.example.andreea.bookhunt.recyclerviewutils.BookAdapter;
import com.example.andreea.bookhunt.recyclerviewutils.ResultsIDBAdapter;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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


    private String originalBookId;
    private String bookTitle;
    private String author;

    private DatabaseReference databaseReviews;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        originalBookId = intent.getStringExtra(Constants.BOOK_ID);
        bookTitle = intent.getStringExtra(Constants.TITLE);
        author = intent.getStringExtra(Constants.AUTHOR);

        initView();

        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");
        firebaseAuth = FirebaseAuth.getInstance();
        reviewArrayList = new ArrayList<>();

        databaseReviews.orderByChild("bookId").equalTo(originalBookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    reviewArrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Review review = ds.getValue(Review.class);
                        reviewArrayList.add(review);
                    }
                    Collections.sort(reviewArrayList, new Comparator<Review>() {
                        @Override
                        public int compare(Review o1, Review o2) {
                            return Long.valueOf(o1.getCreatedAtMiliseconds()).compareTo(o2.getCreatedAtMiliseconds());
                        }
                    });
                    reviewAdapter = new AddReviewAdapter(ReviewActivity.this, reviewArrayList);
                    recyclerViewReviews.setAdapter(reviewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReviewActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReview);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });
        textViewTitle = findViewById(R.id.textViewReview);
        String textViewTitleString = textViewTitle.getText().toString();
        textViewTitle.setText(textViewTitleString + bookTitle + " by " + author);
        ratingBar = findViewById(R.id.ratingBarAddReview);
        buttonAddReviewBH = findViewById(R.id.buttonAddReviewBH);
        buttonAddReviewBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddReviewBHOnClick(originalBookId, bookTitle);
            }
        });
        editTextReview = findViewById(R.id.editTextReview);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.rvAddReview);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
//        reviewArrayList =getReview();
//        reviewAdapter = new AddReviewAdapter(ReviewActivity.this, reviewArrayList);
//        recyclerViewReviews.setAdapter(reviewAdapter);
    }

//    public ArrayList<Review> getReview() {
//        //String mPhotoUrl, String mTitle, String mCountry, double mPrice, double mRating, String typeTrip, String startDate, String endDate
//        ArrayList<Review> reviews = new ArrayList<>();
//        reviews.add(new Review("this book surprised me", "uadina123", (float) 3.5));
//        reviews.add(new Review("is a very good book","ioana",4));
//        reviews.add(new Review("awesome", "andreea123", 3));
//        return reviews;
//    }

    public void btnAddReviewBHOnClick(String originalBookId, String bookTitle) {
        String reviewAdded = editTextReview.getText().toString();
        float rating = ratingBar.getRating();
        Date date = new Date();
        long timeMili = (-1) * date.getTime();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        String reviewId = databaseReviews.push().getKey();
        String username = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, ReviewActivity.this);
        Review review = new Review(reviewAdded, currentUserId, rating, date.toString(), originalBookId, reviewId, timeMili, username, bookTitle);
        databaseReviews.push().setValue(review);
        //reviewArrayList.add(new Review(reviewAdded, "andreea123",rating));
    }
}
