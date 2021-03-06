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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.Notification;
import com.example.andreea.bookhunt.models.ResultIDB;
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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ResultsIDreamBooksActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;

    private String mBookTitle;
    private String mAuthor;
    private String mPhotoUrl;
    private String mSnippet;
    private float mReview;
    private String mSource;

    private float phone_width_dp;
    private float phone_height_dp;

    private ResultIDB mResultIDB;
    private ArrayList<ResultIDB> resultIDBArrayList;
    private ResultsIDBAdapter resultsIDBAdapter;
    private RecyclerView mRecyclerViewResults;

    private FloatingActionButton floatingActionButton;
    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_idream_books);

        Intent intent = getIntent();
        mBookTitle = intent.getStringExtra(Constants.TITLE);
        mAuthor = intent.getStringExtra(Constants.AUTHOR);
        initView();

        initNavDrawer();

        resultIDBArrayList = new ArrayList<>();
        resultIDBArrayList = intent.getParcelableArrayListExtra(Constants.RESULT_ARRAY_IDB);
        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);

//        Picasso.get().load(mPhotoUrl).into((ImageView)findViewById(R.id.imageViewResult));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Books");

        resultsIDBAdapter = new ResultsIDBAdapter(ResultsIDreamBooksActivity.this, resultIDBArrayList);
        mRecyclerViewResults.setAdapter(resultsIDBAdapter);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL_IDREAMBOOKS)
//                .addConverterFactory(SimpleXmlConverterFactory.create())
//                .build();
//
//        IDreamBooksAPI iDreamBooksAPI = retrofit.create(IDreamBooksAPI.class);
//        String titleAuthor = mBookTitle + " " + mAuthor;
//        String titleAuthor1 = titleAuthor.replaceAll(" ", "+");
//        Call<IDreamBooksResponse> call = iDreamBooksAPI.getIDreamBooksResponse(titleAuthor, Constants.DEVELOPER_KEY_IDREAMBOOKS);
//        call.enqueue(new Callback<IDreamBooksResponse>() {
//            @Override
//            public void onResponse(Call<IDreamBooksResponse> call, Response<IDreamBooksResponse> response) {
//                List<CriticReview> criticReviews = response.body().getBookIDB().getCriticReviews().getCriticReview();
//                for(int i = 0; i < criticReviews.size(); i++) {
//                    mSnippet = criticReviews.get(i).getSnippet();
//                    mSource = criticReviews.get(i).getSource();
//                    mReview = criticReviews.get(i).getStarRating();
//                    mResultIDB = new ResultIDB(mSnippet, mSource, mReview);
//                    resultIDBArrayList.add(mResultIDB);
//                }
//                resultsIDBAdapter = new ResultsIDBAdapter(ResultsIDreamBooksActivity.this, resultIDBArrayList);
//                mRecyclerViewResults.setAdapter(resultsIDBAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<IDreamBooksResponse> call, Throwable t) {
//                Log.e("ResultsIDreamBooks", "onFailure: Unable to retrieve RSS: " + t.getMessage());
//                Toast.makeText(ResultsIDreamBooksActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
//            }
//        });
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

                if(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ResultsIDreamBooksActivity.this).equals("0")) {
                    mCounter.setVisibility(View.GONE);
                } else {
                    mCounter.setText(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ResultsIDreamBooksActivity.this));
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
                Intent intent = new Intent(ResultsIDreamBooksActivity.this, NotificationActivity.class);
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
            Intent intent = new Intent(ResultsIDreamBooksActivity.this, NotificationActivity.class);
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
            Intent intent = new Intent(ResultsIDreamBooksActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(ResultsIDreamBooksActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(ResultsIDreamBooksActivity.this, FavouriteActivity.class);
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
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(ResultsIDreamBooksActivity.this);
        Intent intent = new Intent(ResultsIDreamBooksActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void initView() {
        View view_result = findViewById(R.id.content_result);
        view_result.setVisibility(View.GONE);

        View view_index = findViewById(R.id.content_index);
        view_index.setVisibility(View.GONE);

        View view_result_idb = findViewById(R.id.content_result_idb);
        view_result_idb.setVisibility(View.VISIBLE);

        View view_fav = findViewById(R.id.content_fav);
        view_fav.setVisibility(View.GONE);

        View view_bh = findViewById(R.id.bh_content);
        view_bh.setVisibility(View.GONE);

        View view_profile = findViewById(R.id.content_profile);
        view_profile.setVisibility(View.GONE);

        View view_notification = findViewById(R.id.content_notification);
        view_notification.setVisibility(View.GONE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.hide();

        mRecyclerViewResults = (RecyclerView) findViewById(R.id.rvResultsIDB);
        mRecyclerViewResults.setLayoutManager(new LinearLayoutManager(this));

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(mBookTitle + " by " + mAuthor);

    }

    public void initNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("I Dream Books");

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
