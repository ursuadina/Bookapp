package com.example.andreea.bookhunt;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.recyclerviewutils.BookAdapter;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.Methods;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference booksReference;

    private String mCurrentPhotoPath;
    private File photoFile;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;
    private RecyclerView mRecyclerViewBooks;
    private ImageButton mImageButtonDelete;
    private ArrayList<Book> books;
    private View view1;
    private View view2;
    private BookAdapter bookAdapter;

    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        //Methods.checkPermissions(IndexActivity.this, IndexActivity.this);
        Intent intent = getIntent();

        books = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        booksReference = FirebaseDatabase.getInstance().getReference("Books")
                .child(firebaseAuth.getCurrentUser().getUid());

        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    books.add(book);
                }
                bookAdapter = new BookAdapter(getApplicationContext(), books);
                mRecyclerViewBooks.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(IndexActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initView() {
        view1 = findViewById(R.id.content_index);
        view1.setVisibility(View.VISIBLE);

        view2 = findViewById(R.id.content_result);
        view2.setVisibility(View.GONE);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getApplicationContext()));
        mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));
        mRecyclerViewBooks = findViewById(R.id.rvBooks);
        mRecyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            view1 = findViewById(R.id.content_index);
            view1.setVisibility(View.GONE);

//            view2 = findViewById(R.id.content_result);
//            view2.setVisibility(View.VI);
            Intent intent = new Intent(IndexActivity.this, ResultActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_profile) {

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
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(IndexActivity.this);
        Intent intent = new Intent(IndexActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void btnSearchBook(View view) {
        Intent intent = new Intent(IndexActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
