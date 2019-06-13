package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private TextView textViewFirstName;
    private TextView textViewLastName;

    private LinearLayout linearLayoutFN;
    private LinearLayout linearLayoutFNRead;
    private LinearLayout linearLayoutLN;
    private LinearLayout linearLayoutLNRead;

    private EditText textInputEditTextFirstName;
    private EditText textInputEditTextLastName;

    private FirebaseAuth firebaseAuth;

    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        initView();

        initNavDrawer();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void initNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {

        View view_index = findViewById(R.id.content_index);
        view_index.setVisibility(View.GONE);

        View view_result = findViewById(R.id.content_result);
        view_result.setVisibility(View.GONE);

        View view_result_idb = findViewById(R.id.content_result_idb);
        view_result_idb.setVisibility(View.GONE);

        View view_fav = findViewById(R.id.content_fav);
        view_fav.setVisibility(View.GONE);

        View view_bh = findViewById(R.id.bh_content);
        view_bh.setVisibility(View.GONE);

        View view_profile = findViewById(R.id.content_profile);
        view_profile.setVisibility(View.VISIBLE);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getApplicationContext()));
        mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));

        textViewFirstName = (TextView) findViewById(R.id.textViewFirstNameEdit);
        textViewLastName = (TextView) findViewById(R.id.textViewLastNameEdit);

        textInputEditTextFirstName = (EditText) findViewById(R.id.teFirstNameEditTextInput);
        textInputEditTextLastName = (EditText) findViewById(R.id.teLastNameEditTextInput);

        linearLayoutFN = (LinearLayout) findViewById(R.id.lnFN);
        linearLayoutFNRead = (LinearLayout) findViewById(R.id.lnFNRead);
        linearLayoutLN = (LinearLayout) findViewById(R.id.lnLN);
        linearLayoutLNRead = (LinearLayout) findViewById(R.id.lnLNRead);


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
            Intent intent = new Intent(ProfileActivity.this, IndexActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {


        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(ProfileActivity.this, FavouriteActivity.class);
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
        SharedPreferencesHelper.deleteAllValuesFromSharedPreferences(ProfileActivity.this);
        Intent intent = new Intent(ProfileActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();

    }

    public void btnSaveFN(View view) {
        String fnEdited = textInputEditTextFirstName.getText().toString();
        textViewFirstName.setText(fnEdited);
        linearLayoutFNRead.setVisibility(View.VISIBLE);
        linearLayoutFN.setVisibility(View.GONE);
    }

    public void btnEditFN(View view) {
        linearLayoutFN.setVisibility(View.VISIBLE);
        linearLayoutFNRead.setVisibility(View.GONE);
    }

    public void btnEditLN(View view) {
        linearLayoutLN.setVisibility(View.VISIBLE);
        linearLayoutLNRead.setVisibility(View.GONE);
    }

    public void btnSaveLN(View view) {
        String lnEdited = textInputEditTextLastName.getText().toString();
        textViewLastName.setText(lnEdited);
        linearLayoutLNRead.setVisibility(View.VISIBLE);
        linearLayoutLN.setVisibility(View.GONE);
    }

    public void btnEditEmail(View view) {
    }

    public void btnSaveEmail(View view) {
    }

    public void btnEditPass(View view) {
    }

    public void btnSavePassword(View view) {
    }


}
