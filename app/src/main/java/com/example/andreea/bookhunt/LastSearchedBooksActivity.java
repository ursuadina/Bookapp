package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.LastSearchBook;
import com.example.andreea.bookhunt.recyclerviewutils.LastSearchedBooksAdapter;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class LastSearchedBooksActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private String title;
    private String author;
    private String lastSearchDate;
    private String username;

    private RecyclerView mRecyclerViewLastBooks;
    private LastSearchedBooksAdapter lastSearchedBooksAdapter;
    private ArrayList<LastSearchBook> lastSearchBooks;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_searched_books);
        initNavDrawer();
        initView();

        firebaseAuth = FirebaseAuth.getInstance();

        lastSearchBooks = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Books").orderByChild("lastSearch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    lastSearchBooks.clear();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            book = ds1.getValue(Book.class);
                            long lastSearch = book.getLastSearch() * (-1);
                            Date date = new Date();
                            long now = date.getTime();
                            if (lastSearch > now - 86400000 && lastSearch < now) {
                                title = book.getBookTitle();
                                author = book.getAuthor();
                                lastSearchDate = book.getLastSearchDate();
                                username = book.getUsername();
                                LastSearchBook lastSearchBook = new LastSearchBook(title, author, lastSearchDate, username, lastSearch);
                                lastSearchBooks.add(lastSearchBook);

                            }
                        }
                    }
                    Collections.sort(lastSearchBooks, new Comparator<LastSearchBook>() {
                        @Override
                        public int compare(LastSearchBook o1, LastSearchBook o2) {
                            return Long.valueOf(o2.getLastSearchLong()).compareTo(o1.getLastSearchLong());
                        }
                    });
                    if(lastSearchBooks.size() == 0) {
                        TextView textView = (TextView) findViewById(R.id.no_books_searched);
                        textView.setVisibility(View.VISIBLE);
                        mRecyclerViewLastBooks.setVisibility(View.GONE);
                    } else {
                        TextView textView = (TextView) findViewById(R.id.no_books_searched);
                        textView.setVisibility(View.GONE);
                        mRecyclerViewLastBooks.setVisibility(View.VISIBLE);
                        lastSearchedBooksAdapter = new LastSearchedBooksAdapter(LastSearchedBooksActivity.this, lastSearchBooks);
                        mRecyclerViewLastBooks.setAdapter(lastSearchedBooksAdapter);
                    }
                } else {
                    TextView textView = (TextView) findViewById(R.id.no_books_searched);
                    textView.setVisibility(View.VISIBLE);
                    mRecyclerViewLastBooks.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        View view_administrator = findViewById(R.id.content_administrator);
        view_administrator.setVisibility(View.GONE);

        View view_last_connected_users = findViewById(R.id.content_last_connected_users);
        view_last_connected_users.setVisibility(View.GONE);

        View view_genres = findViewById(R.id.content_genres);
        view_genres.setVisibility(View.GONE);

        View view_last_searched_books = findViewById(R.id.content_last_searched_books);
        view_last_searched_books.setVisibility(View.VISIBLE);

        View view_loggings_last_week = findViewById(R.id.content_loggings_last_week);
        view_loggings_last_week.setVisibility(View.GONE);

        mRecyclerViewLastBooks = findViewById(R.id.rvLastBooks);
        mRecyclerViewLastBooks.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        NavigationView mNavigationView = (NavigationView) findViewById(R.id.admin_nav_view);
        LinearLayout mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        TextView mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvAdminUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getApplicationContext()));
        TextView mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvAdminEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.administrator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin_action_settings) {
            Intent intent = new Intent(LastSearchedBooksActivity.this, AdministratorActivity.class);
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

        if (id == R.id.nav_books_searched) {
            Intent intent = new Intent(LastSearchedBooksActivity.this, LastSearchedBooksActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_last_users) {
            Intent intent = new Intent(LastSearchedBooksActivity.this, LastConnectedUsersActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_books_domains) {
            Intent intent = new Intent(LastSearchedBooksActivity.this, GenresActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_loggings) {
            Intent intent = new Intent(LastSearchedBooksActivity.this, LoggingsLastWeekActivity.class);
            startActivity(intent);
        } else if (id == R.id.admin_nav_log_out) {
            btnLogOutClick();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(LastSearchedBooksActivity.this);
        Intent intent = new Intent(LastSearchedBooksActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }
}
