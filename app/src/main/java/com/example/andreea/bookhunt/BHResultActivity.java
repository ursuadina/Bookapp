package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.OriginalBooks;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.models.Review;
import com.example.andreea.bookhunt.recyclerviewutils.AddReviewAdapter;
import com.example.andreea.bookhunt.recyclerviewutils.ResultsIDBAdapter;
import com.example.andreea.bookhunt.retrofitUtils.IDreamBooksAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.IDreamBooksResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.criticReview.CriticReview;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BHResultActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseOriginalBooks;
    private DatabaseReference databaseReviews;

    private String bookId;
    private String mBookTitle;
    private String mAuthor;
    private String mPhotoUrl;
    private String review_widget;
    private String originalBookId;
    private String description_new;
    private String titleAuthor;
    private float average_rating;

    private Review mReview;
    private ArrayList<Review> reviewArrayList;
    private AddReviewAdapter reviewAdapter;
    private RecyclerView recyclerViewReviews;
    private Button buttonAddReviewBH;
    private EditText editTextReview;
    private TextView textViewNoReviews;
    private FloatingActionButton floatingActionButton;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhresult);
        Intent intent = getIntent();

        mBookTitle = intent.getStringExtra(Constants.TITLE);
        mAuthor = intent.getStringExtra(Constants.AUTHOR);
        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);
        titleAuthor = mBookTitle + "_" + mAuthor;

        initView();

        initNavDrawer();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReviews = FirebaseDatabase.getInstance().getReference("Reviews");
        databaseOriginalBooks = FirebaseDatabase.getInstance().getReference("OriginalBooks");

        reviewArrayList = new ArrayList<>();
        Query query1 = databaseOriginalBooks.orderByChild("titleAuthor").equalTo(titleAuthor);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    originalBookId = databaseOriginalBooks.push().getKey();
                    OriginalBooks originalBooks = new OriginalBooks(originalBookId, mBookTitle, description_new, mAuthor, titleAuthor);
                    databaseOriginalBooks.push().setValue(originalBooks);
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        originalBookId = ds.getValue(OriginalBooks.class).getBookId();
                    }
                }
                Query query = databaseReviews.orderByChild("bookId").equalTo(originalBookId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            textViewNoReviews.setVisibility(View.VISIBLE);
                        } else {
                            textViewNoReviews.setVisibility(View.GONE);
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Review review = ds.getValue(Review.class);
                                reviewArrayList.add(review);
                            }
                            reviewAdapter = new AddReviewAdapter(BHResultActivity.this, reviewArrayList);
                            recyclerViewReviews.setAdapter(reviewAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BHResultActivity.this, "nu s-a gasit", Toast.LENGTH_SHORT).show();
            }
        });
//        reviewAdapter = new AddReviewAdapter(BHResultActivity.this, reviewArrayList);
//        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(BHResultActivity.this, FavouriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            btnLogOutClick();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(BHResultActivity.this);
        Intent intent = new Intent(BHResultActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void initView() {
        View view_result = findViewById(R.id.content_result);
        view_result.setVisibility(View.GONE);

        View view_index = findViewById(R.id.content_index);
        view_index.setVisibility(View.GONE);

        View view_result_idb = findViewById(R.id.content_result_idb);
        view_result_idb.setVisibility(View.GONE);

        View view_fav = findViewById(R.id.content_fav);
        view_fav.setVisibility(View.GONE);

        View view_bh = findViewById(R.id.bh_content);
        view_bh.setVisibility(View.VISIBLE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.hide();

        recyclerViewReviews = (RecyclerView) findViewById(R.id.rvResultsBH);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        textViewNoReviews = (TextView) findViewById(R.id.no_reviews);

    }

    public void initNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getApplicationContext()));
        mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));
    }

//    public ArrayList<Review> getReview(String titleAuthor, final DatabaseReference databaseReviews, final DatabaseReference databaseOriginalBooks) {
//        ArrayList<Review> reviews = new ArrayList<>();
//        Query query1 = databaseOriginalBooks.orderByChild("titleAuthor").equalTo(titleAuthor);
//        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    originalBookId = databaseOriginalBooks.push().getKey();
//                    OriginalBooks originalBooks = new OriginalBooks(originalBookId, mBookTitle, description_new, mAuthor, titleAuthor);
//                    databaseOriginalBooks.push().setValue(originalBooks);
//                } else {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        originalBookId = ds.getValue(OriginalBooks.class).getBookId();
//                    }
//                }
//                Query query = databaseReviews.orderByChild("bookId").equalTo(originalBookId);
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                })
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(BHResultActivity.this, "nu s-a gasit", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return reviews;
//    }

}