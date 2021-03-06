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
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.Notification;
import com.example.andreea.bookhunt.models.OriginalBooks;
import com.example.andreea.bookhunt.retrofitUtils.GoodreadsAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.Convertor;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ResultActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;
    private DatabaseReference databaseOriginalBooks;

    private View view_index;
    private View view_result;
    private View view_result_idb;
    private View view_fav;
    private RatingBar ratingBarGoodReads;

    private String bookId;
    private String mBookTitle;
    private String mAuthor;
    private String mPhotoUrl;
    private String review_widget;
    private String originalBookId;
    private String description_new;
    private String titleAuthor;
    private float average_rating;

    private float phone_width_dp;
    private float phone_height_dp;

    private Book mBook;

    private WebView engine;
    private FloatingActionButton floatingActionButton;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        initView();

        initNavDrawer();

        getPhoneDimensions();

        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);
//        Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Books");
        databaseOriginalBooks = FirebaseDatabase.getInstance().getReference("OriginalBooks");

        review_widget = intent.getStringExtra(Constants.REVIEW_WIDGET);
        String review_widget2 = review_widget.replaceAll("width=\"565\" height=\"400\"", "width=\"" + phone_width_dp + "\" height=\"" + phone_height_dp + "\"");
        String review_widget_final = review_widget2.replaceAll("width:565px;", "width:" + phone_width_dp + "px;");
        engine.loadData(review_widget_final, "text/html", "UTF-8");

        Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));

        average_rating = intent.getFloatExtra(Constants.AVERAGE_RATING, 0);
        ratingBarGoodReads.setVisibility(View.VISIBLE);
        ratingBarGoodReads.setRating(average_rating);
        ratingBarGoodReads.setStepSize((float) 0.5);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL_GOODREADS)
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build();
//
//        GoodreadsAPI goodreadsAPI = retrofit.create(GoodreadsAPI.class);
//        Call<GoodreadsResponse> call = goodreadsAPI.getGoodreadsResponse(Constants.DEVELOPER_KEY_GOODREADS, mBookTitle, mAuthor);
//        call.enqueue(new Callback<GoodreadsResponse>() {
//            @Override
//            public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {
//                review_widget = response.body().getBook().getReviews_widget();
//                String review_widget2 = review_widget.replaceAll("width=\"565\" height=\"400\"", "width=\"" + phone_width_dp + "\" height=\"" + phone_height_dp + "\"");
//                String review_widget_final = review_widget2.replaceAll("width:565px;", "width:" + phone_width_dp + "px;");
//                engine.loadData(review_widget_final, "text/html", "UTF-8");
//
//                Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));
//
//                average_rating = response.body().getBook().getAverage_rating();
//                ratingBarGoodReads.setVisibility(View.VISIBLE);
//                ratingBarGoodReads.setRating(average_rating);
//                ratingBarGoodReads.setStepSize((float) 0.5);
//
//                bookId = databaseBooks.push().getKey();
//                String description = response.body().getBook().getDescription();
//                description_new = description.replaceAll("<br />", "\n");
//
//                titleAuthor = mBookTitle + "_" + mAuthor;
//                Query query1 = databaseOriginalBooks.orderByChild("titleAuthor").equalTo(titleAuthor);
//                query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (!dataSnapshot.exists()) {
//                            originalBookId = databaseOriginalBooks.push().getKey();
//                            OriginalBooks originalBooks = new OriginalBooks(originalBookId, mBookTitle, description_new, mAuthor, titleAuthor);
//                            databaseOriginalBooks.push().setValue(originalBooks);
//                        } else {
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                originalBookId = ds.getValue(OriginalBooks.class).getBookId();
//                            }
//                        }
//                        mBook = new Book(bookId, mBookTitle, mAuthor, mPhotoUrl, average_rating, description_new, originalBookId);
//                        databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                .child(bookId).setValue(mBook);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Toast.makeText(ResultActivity.this, "nu s-a gasit", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                Log.d("ResultActivity", "onResponse: GoodReadsReasponse: " + response.body().getBook());
//                Log.d("ResultActivity", "onResponse: Server Response: " + response.toString());
//            }
//
//            @Override
//            public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
//                Log.e("ResultActivity", "onFailure: Unable to retrieve RSS: " + t.getMessage());
//                Toast.makeText(ResultActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void getPhoneDimensions() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float phone_width = displayMetrics.widthPixels - 100;
        float phone_height = displayMetrics.heightPixels - 500;

        phone_width_dp = Convertor.convertPixelsToDp(phone_width);
        phone_height_dp = Convertor.convertPixelsToDp(phone_height) - 200;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        final RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.action_settings).getActionView();
        final TextView mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
        FirebaseDatabase.getInstance().getReference("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ResultActivity.this).equals("0")) {
                    mCounter.setVisibility(View.GONE);
                } else {
                    mCounter.setText(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ResultActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageButton imageButton = (ImageButton) badgeLayout.findViewById(R.id.ibNotif);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(ResultActivity.this, NotificationActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(ResultActivity.this, IndexActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(ResultActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(ResultActivity.this, FavouriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            btnLogOutClick();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(ResultActivity.this);
        Intent intent = new Intent(ResultActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void initView() {
        view_result = findViewById(R.id.content_result);
        view_result.setVisibility(View.VISIBLE);

        view_index = findViewById(R.id.content_index);
        view_index.setVisibility(View.GONE);

        view_result_idb = findViewById(R.id.content_result_idb);
        view_result_idb.setVisibility(View.GONE);

        view_fav = findViewById(R.id.content_fav);
        view_fav.setVisibility(View.GONE);

        View view_bh = findViewById(R.id.bh_content);
        view_bh.setVisibility(View.GONE);

        View view_profile = findViewById(R.id.content_profile);
        view_profile.setVisibility(View.GONE);

        View view_notification = findViewById(R.id.content_notification);
        view_notification.setVisibility(View.GONE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.hide();

        ratingBarGoodReads = (RatingBar) findViewById(R.id.ratingBarGoodReads);
        ratingBarGoodReads.setVisibility(View.GONE);

        engine = (WebView) findViewById(R.id.web_engine);
        engine.getSettings().setJavaScriptEnabled(true);
        engine.getSettings().setPluginState(WebSettings.PluginState.ON);
        engine.getSettings().setAllowFileAccess(true);

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
}
