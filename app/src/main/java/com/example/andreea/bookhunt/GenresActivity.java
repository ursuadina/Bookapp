package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;

public class GenresActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);
        initNavDrawer();
        initView();

        firebaseAuth = FirebaseAuth.getInstance();

    }

    private void initView() {
        View view_administrator = findViewById(R.id.content_administrator);
        view_administrator.setVisibility(View.GONE);

        View view_last_connected_users = findViewById(R.id.content_last_connected_users);
        view_last_connected_users.setVisibility(View.GONE);

        View view_genres = findViewById(R.id.content_genres);
        view_genres.setVisibility(View.VISIBLE);

        View view_last_searched_books = findViewById(R.id.content_last_searched_books);
        view_last_searched_books.setVisibility(View.GONE);

        View view_loggings_last_week = findViewById(R.id.content_loggings_last_week);
        view_loggings_last_week.setVisibility(View.GONE);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.admin_nav_log_out) {
            btnLogOutClick();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.admin_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(GenresActivity.this);
        Intent intent = new Intent(GenresActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void btnAddGenre(View view) {
        Intent intent = new Intent(GenresActivity.this, AddGenreActivity.class);
        startActivity(intent);
    }

    public void btnViewGenres(View view) {
    }

    public void btnViewGraph(View view) {
        Intent intent = new Intent(GenresActivity.this, GenresGraphActivity.class);
        startActivity(intent);
    }
}
