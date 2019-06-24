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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.recyclerviewutils.BookAdapter;
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

public class FavouriteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerViewFav;
    private BookAdapter bookAdapter;
    private ArrayList<Book> books;

    private FirebaseAuth firebaseAuth;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;
    private DatabaseReference databaseFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        initView();

        initNavDrawer();
        books = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
//        databaseFavourite = FirebaseDatabase.getInstance().getReference("Favourite")
//                 .child(firebaseAuth.getCurrentUser().getUid());
        databaseFavourite = FirebaseDatabase.getInstance().getReference("Books/" + firebaseAuth.getCurrentUser().getUid());
        Query query = databaseFavourite.orderByChild("fav").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    books.add(book);
                }
                if (books.size() == 0) {
                    TextView textViewNoFav = (TextView) findViewById(R.id.textViewNoFav);
                    textViewNoFav.setVisibility(View.VISIBLE);
                } else {
                    TextView textViewNoFav = (TextView) findViewById(R.id.textViewNoFav);
                    textViewNoFav.setVisibility(View.GONE);
                }
                bookAdapter = new BookAdapter(FavouriteActivity.this, books);
                recyclerViewFav.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavouriteActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
//        books.add(new Book("-Lh18HVhZkgpGeskoKUS", "To kill a mockingbird", "Harper Lee", "https://firebasestorage.googleapis.com/v0/b/book-hunt.appspot.com/o/images%2Fusers%2Fve00qQvfE7NLggglinqwxSIGW1U2%2FTo%20kill%20a%20mockingbirdHarper%20Lee20190610_234625.jpg?alt=media&token=ae65f60e-fcb9-4985-ac84-3620f68be1e9"
//                , (float) 4.26, "The unforgettable novel of a childhood in a sleepy Southern town and the crisis of conscience that rocked it, To Kill A Mockingbird became both an instant bestseller and a critical success when it was first published in 1960. It went on to win the Pulitzer Prize in 1961 and was later made into an Academy Award-winning film, also a classic.\\n\\nCompassionate, dramatic, and deeply moving, To Kill A Mockingbird takes readers to the roots of human behavior - to innocence and experience, kindness and cruelty, love and hatred, humor and pathos. Now with over 18 million copies in print and translated into forty languages, this regional story by a young Alabama woman claims universal appeal. Harper Lee always considered her book to be a simple love story. Today it is regarded as a masterpiece of American literature."));
//        bookAdapter = new BookAdapter(FavouriteActivity.this, books);
//        recyclerViewFav.setAdapter(bookAdapter);
    }

    private void initView() {
        View view_result = findViewById(R.id.content_result);
        view_result.setVisibility(View.GONE);

        View view_index = findViewById(R.id.content_index);
        view_index.setVisibility(View.GONE);

        View view_result_idb = findViewById(R.id.content_result_idb);
        view_result_idb.setVisibility(View.GONE);

        View view_fav = findViewById(R.id.content_fav);
        view_fav.setVisibility(View.VISIBLE);

        View view_bh = findViewById(R.id.bh_content);
        view_bh.setVisibility(View.GONE);

        View view_profile = findViewById(R.id.content_profile);
        view_profile.setVisibility(View.GONE);

        View view_notification = findViewById(R.id.content_notification);
        view_notification.setVisibility(View.GONE);

        recyclerViewFav = findViewById(R.id.rvFav);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.hide();
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

                if(SharedPreferencesHelper.getStringValueForUserInfo("Notification", FavouriteActivity.this).equals("0")) {
                    mCounter.setVisibility(View.GONE);
                } else {
                    mCounter.setText(SharedPreferencesHelper.getStringValueForUserInfo("Notification", FavouriteActivity.this));
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
                Intent intent = new Intent(FavouriteActivity.this, NotificationActivity.class);
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
            Intent intent = new Intent(FavouriteActivity.this, NotificationActivity.class);
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
            Intent intent = new Intent(FavouriteActivity.this, IndexActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(FavouriteActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_fav) {

        } else if (id == R.id.nav_log_out) {
            btnLogOutClick();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(FavouriteActivity.this);
        Intent intent = new Intent(FavouriteActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

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
