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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andreea.bookhunt.models.User;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.EmailHelper;
import com.example.andreea.bookhunt.utils.NameHelper;
import com.example.andreea.bookhunt.utils.PasswordHelper;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewEmail;
    private TextView textViewPass;
    private TextView textViewConfPass;

    private LinearLayout linearLayoutFN;
    private LinearLayout linearLayoutFNRead;
    private LinearLayout linearLayoutLN;
    private LinearLayout linearLayoutLNRead;
    private LinearLayout linearLayoutEmail;
    private LinearLayout linearLayoutEmailRead;
    private LinearLayout linearLayoutPass;
    private LinearLayout linearLayoutPassRead;
    private LinearLayout linearLayoutConfPass;

    private EditText textInputEditTextFirstName;
    private EditText textInputEditTextLastName;
    private EditText textInputEditTextEmail;
    private EditText textInputEditTextPass;
    private EditText textInputEditTextConfPass;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("Users");

        initView();

        initNavDrawer();

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

        View view_notification = findViewById(R.id.content_notification);
        view_notification.setVisibility(View.GONE);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.hide();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        LinearLayout mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        TextView mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getApplicationContext()));
        TextView mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));


        textViewFirstName = (TextView) findViewById(R.id.textViewFirstNameEdit);
        textViewLastName = (TextView) findViewById(R.id.textViewLastNameEdit);
        textViewEmail = (TextView) findViewById(R.id.textViewEmailEdit);
        textViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getApplicationContext()));
        textViewPass = (TextView) findViewById(R.id.textViewPassEdit);
        textViewPass.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.PASS, getApplicationContext()));
        textViewConfPass = (TextView) findViewById(R.id.tvConfPass);

        databaseReferenceUsers.orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                textViewFirstName.setText(user.getFirstName());
                                textViewLastName.setText(user.getLastName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        textInputEditTextFirstName = (EditText) findViewById(R.id.teFirstNameEditTextInput);
        textInputEditTextLastName = (EditText) findViewById(R.id.teLastNameEditTextInput);
        textInputEditTextEmail= (EditText) findViewById(R.id.teEmailEditTextInput);
        textInputEditTextPass = (EditText) findViewById(R.id.tePassEditTextInput);
        textInputEditTextConfPass = (EditText) findViewById(R.id.teConfPassEditTextInput);

        linearLayoutFN = (LinearLayout) findViewById(R.id.lnFN);
        linearLayoutFNRead = (LinearLayout) findViewById(R.id.lnFNRead);
        linearLayoutLN = (LinearLayout) findViewById(R.id.lnLN);
        linearLayoutLNRead = (LinearLayout) findViewById(R.id.lnLNRead);
        linearLayoutEmail = (LinearLayout) findViewById(R.id.lnEmail);
        linearLayoutEmailRead = (LinearLayout) findViewById(R.id.lnEmailRead);
        linearLayoutPass = (LinearLayout) findViewById(R.id.lnPass);
        linearLayoutPassRead = (LinearLayout) findViewById(R.id.lnPassRead);
        linearLayoutConfPass = (LinearLayout) findViewById(R.id.lnConfPass);

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
        final RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.action_settings).getActionView();
        final TextView mCounter = (TextView) badgeLayout.findViewById(R.id.counter);
        FirebaseDatabase.getInstance().getReference("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ProfileActivity.this).equals("0")) {
                    mCounter.setVisibility(View.GONE);
                } else {
                    mCounter.setText(SharedPreferencesHelper.getStringValueForUserInfo("Notification", ProfileActivity.this));
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
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
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
            Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
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
        if (NameHelper.isNameValid(fnEdited)) {
            textViewFirstName.setText(fnEdited);
            linearLayoutFNRead.setVisibility(View.VISIBLE);
            linearLayoutFN.setVisibility(View.GONE);
        } else {
            textInputEditTextFirstName.setError(getResources().getString(R.string.error_name_input));
        }
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
        if (NameHelper.isNameValid(lnEdited)) {
            textViewLastName.setText(lnEdited);
            linearLayoutLNRead.setVisibility(View.VISIBLE);
            linearLayoutLN.setVisibility(View.GONE);
        } else {
            textInputEditTextLastName.setError(getResources().getString(R.string.error_name_input));
        }
    }

    public void btnEditEmail(View view) {
        linearLayoutEmail.setVisibility(View.VISIBLE);
        linearLayoutEmailRead.setVisibility(View.GONE);
    }

    public void btnSaveEmail(View view) {
        String emailEdited = textInputEditTextEmail.getText().toString();
        if(EmailHelper.isEmailValid(emailEdited)) {
            textViewEmail.setText(emailEdited);
            linearLayoutEmailRead.setVisibility(View.VISIBLE);
            linearLayoutEmail.setVisibility(View.GONE);
        } else {
            textInputEditTextEmail.setError(getResources().getString(R.string.error_email_input));
        }
    }

    public void btnEditPass(View view) {
        linearLayoutPass.setVisibility(View.VISIBLE);
        linearLayoutPassRead.setVisibility(View.GONE);
        linearLayoutConfPass.setVisibility(View.VISIBLE);
        textViewConfPass.setVisibility(View.VISIBLE);
    }

    public void btnSavePassword(View view) {
        String passEdited = textInputEditTextPass.getText().toString();
        String confPassEdited = textInputEditTextConfPass.getText().toString();
        if(PasswordHelper.isPasswordValid(passEdited)) {
            if (PasswordHelper.isConfirmationPassValid(passEdited, confPassEdited)) {
                textViewPass.setText(passEdited);
                linearLayoutPassRead.setVisibility(View.VISIBLE);
                linearLayoutPass.setVisibility(View.GONE);
                linearLayoutConfPass.setVisibility(View.GONE);
                textViewConfPass.setVisibility(View.GONE);
            } else {
                textInputEditTextConfPass.setError(getResources().getString(R.string.error_conf_password_input));
            }
        } else {
            textInputEditTextPass.setError(getResources().getString(R.string.error_password_input));
        }
    }


    public void btnSaveInfo(View view) {
        final String emailChanged = textViewEmail.getText().toString();
        String passwordChanged = textViewPass.getText().toString();
        firebaseAuth.getCurrentUser().updatePassword(passwordChanged);
        firebaseAuth.getCurrentUser().updateEmail(emailChanged);
        databaseReferenceUsers.orderByKey().equalTo(firebaseAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                User user = ds.getValue(User.class);
                                if (! user.getEmail().equals(textViewEmail.getText().toString())) {
                                    user.setEmail(textViewEmail.getText().toString());
                                }
                                if(! user.getFirstName().equals(textViewFirstName.getText().toString())) {
                                    user.setFirstName(textViewFirstName.getText().toString());
                                }
                                if(!user.getLastName().equals(textViewLastName.getText().toString())) {
                                    user.setLastName(textViewLastName.getText().toString());
                                }
                                databaseReferenceUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
