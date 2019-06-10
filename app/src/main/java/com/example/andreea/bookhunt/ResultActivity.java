package com.example.andreea.bookhunt;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.retrofitUtils.GoodreadsAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.Convertor;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ResultActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;

    private View view_index;
    private View view_result;
    private View view_result_idb;
    private View view_fav;
    private RatingBar ratingBarGoodReads;

    private String mBookTitle;
    private String mAuthor;
    private String mPhotoUrl;
    private String review_widget;

    private float phone_width_dp;
    private float phone_height_dp;

    private Book mBook;

    private WebView engine;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();

        initView();

        initNavDrawer();

        getPhoneDimensions();

        mBookTitle = intent.getStringExtra(Constants.TITLE);
        mAuthor = intent.getStringExtra(Constants.AUTHOR);
        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);
//        Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Books");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_GOODREADS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        GoodreadsAPI goodreadsAPI = retrofit.create(GoodreadsAPI.class);
        Call<GoodreadsResponse> call = goodreadsAPI.getGoodreadsResponse(Constants.DEVELOPER_KEY_GOODREADS, mBookTitle, mAuthor);
        call.enqueue(new Callback<GoodreadsResponse>() {
            @Override
            public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {
                review_widget = response.body().getBook().getReviews_widget();
                String review_widget2 = review_widget.replaceAll("width=\"565\" height=\"400\"", "width=\"" + phone_width_dp + "\" height=\"" + phone_height_dp + "\"");
                String review_widget_final = review_widget2.replaceAll("width:565px;", "width:" + phone_width_dp + "px;");
                engine.loadData(review_widget_final, "text/html", "UTF-8");

                Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));

                float average_rating = response.body().getBook().getAverage_rating();
                ratingBarGoodReads.setVisibility(View.VISIBLE);
                ratingBarGoodReads.setRating(average_rating);
                ratingBarGoodReads.setStepSize((float) 0.5);

                String id = databaseBooks.push().getKey();
                String description = response.body().getBook().getDescription();
                String description_new = description.replaceAll("<br />", "\n");

                mBook = new Book(id, mBookTitle, mAuthor, mPhotoUrl, average_rating, description_new);
                databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(id).setValue(mBook);

                Log.d("ResultActivity", "onResponse: GoodReadsReasponse: " + response.body().getBook());
                Log.d("ResultActivity", "onResponse: Server Response: " + response.toString());
            }

            @Override
            public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
                Log.e("ResultActivity", "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(ResultActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(ResultActivity.this, FavouriteActivity.class);
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
    }
}
