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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andreea.bookhunt.models.User;
import com.example.andreea.bookhunt.recyclerviewutils.BookAdapter;
import com.example.andreea.bookhunt.recyclerviewutils.UserAdapter;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class LastConnectedUsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth firebaseAuth;

    private RecyclerView mRecyclerViewUsers;
    private UserAdapter userAdapter;
    private User user;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_connected_users);

        Intent intent = getIntent();

        initNavDrawer();
        initView();
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users").orderByChild("lastLoggedIn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    users.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        user = ds.getValue(User.class);
                        long lastLoggedIn = user.getLastLoggedIn() * (-1);
                        Date date = new Date();
                        long now = date.getTime();
                        if(!user.getUsername().equals("administrator") && lastLoggedIn > now - 86400000 && lastLoggedIn < now) {
                            users.add(user);
                        }
                    }
                    userAdapter = new UserAdapter(LastConnectedUsersActivity.this, users);
                    mRecyclerViewUsers.setAdapter(userAdapter);
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
        view_last_connected_users.setVisibility(View.VISIBLE);

        View view_genres = findViewById(R.id.content_genres);
        view_genres.setVisibility(View.GONE);

        View view_last_searched_books = findViewById(R.id.content_last_searched_books);
        view_last_searched_books.setVisibility(View.GONE);

        mRecyclerViewUsers = findViewById(R.id.rvUsers);
        mRecyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
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

    public void initNavDrawer() {
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

    public void btnLogOutClick() {
        firebaseAuth.signOut();
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(LastConnectedUsersActivity.this);
        Intent intent = new Intent(LastConnectedUsersActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }
}
