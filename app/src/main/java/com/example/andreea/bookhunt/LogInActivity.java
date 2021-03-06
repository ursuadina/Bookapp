package com.example.andreea.bookhunt;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.LogInUser;
import com.example.andreea.bookhunt.models.User;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogInActivity extends AppCompatActivity{

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private CheckBox mCheckBoxRemember;
    private SignInButton signInButton;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private User mUser;

    private ProgressBar progressBar;
    private ConstraintLayout layout;
    private boolean isConnected;
    private boolean enterOnCreate;
    private String data;
    private String startData;
    private BroadcastReceiver state = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!enterOnCreate) {
                ConnectivityManager check = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                Network[] info = check.getAllNetworks();
                NetworkInfo networkInfo;
                isConnected = false;
                for (int i = 0; i < info.length; i++) {
                    networkInfo = check.getNetworkInfo(info[i]);
                    if (networkInfo.isConnected()) {
                        isConnected = true;

                    }
                }
                if (isConnected) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .requestProfile()
                            .build();

                    googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

                    if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.REMEMBER, LogInActivity.this).equals("True")) {
                        Date date = new Date();
                        SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "true",LogInActivity.this);
                        FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastLoggedIn").setValue((-1)*date.getTime());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                        data = formatter1.format(date);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        c.add(Calendar.DATE,  -8);
                        Date start = c.getTime();
                        startData = formatter1.format(start);
                        SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "False",LogInActivity.this);
                        FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastDate").setValue(formatter.format(date));
                        FirebaseDatabase.getInstance().getReference("LogIns").child(data).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("False")) {
                                    if (dataSnapshot.exists()) {
                                        String username = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
                                        boolean found = false;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getValue(LogInUser.class).getUsername().equals(username)) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            LogInUser logInUser = new LogInUser();
                                            logInUser.setUsername(username);
                                            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                            FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                        }
                                    } else {
                                        LogInUser logInUser = new LogInUser();
                                        logInUser.setUsername(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this));
                                        SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                        FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                    }

                                    if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this).equals("administrator")) {
                                        Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    FirebaseDatabase.getInstance().getReference("LogIns").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.getChildrenCount() == 8) {
                                                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                FirebaseDatabase.getInstance().getReference("LogIns").child(startData).removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//                        String currentUser = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
//                        FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
//                                .equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.exists()) {
//                                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
//                                        id = ds.getKey();
//                                        mUser = ds.getValue(User.class);
//                                        Date date = new Date();
//                                        mUser.setLastLoggedIn((-1) * date.getTime());
//                                        FirebaseDatabase.getInstance().getReference("Users/" + id).setValue(mUser);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
                        if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this).equals("administrator")) {
                            Intent intentAdmin = new Intent(LogInActivity.this, AdministratorActivity.class);
                            startActivity(intentAdmin);
                            finish();
                        } else {
                            Intent intentUser = new Intent(LogInActivity.this, IndexActivity.class);
                            startActivity(intentUser);
                            finish();
                        }
                    } else {
                        setContentView(R.layout.activity_log_in);

                        initView();
                        mUser = new User();

                    }
                } else {
                    setContentView(R.layout.progress_layout);
                }
            } else {
                enterOnCreate = false;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        IntentFilter intentFilter1 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(state, intentFilter1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enterOnCreate = true;
        //Methods.checkPermissions(LogInActivity.this, LogInActivity.this);
        isConnected = false;
        ConnectivityManager check = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] info = check.getAllNetworks();
        NetworkInfo networkInfo;
        for (int i = 0; i < info.length; i++) {
            networkInfo = check.getNetworkInfo(info[i]);
            if (networkInfo.isConnected()) {
                isConnected = true;

            }
        }

        if (isConnected) {

            firebaseAuth = FirebaseAuth.getInstance();
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build();

            googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), googleSignInOptions);

            if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.REMEMBER, LogInActivity.this).equals("True")) {
                Date date = new Date();
                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "true", LogInActivity.this);
                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastLoggedIn").setValue((-1) * date.getTime());
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                data = formatter1.format(date);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DATE, -7);
                Date start = c.getTime();
                startData = formatter1.format(start);
                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "False", LogInActivity.this);
                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastDate").setValue(formatter.format(date));
                FirebaseDatabase.getInstance().getReference("LogIns").child(data).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("False")) {
                            if (dataSnapshot.exists()) {
                                String username = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
                                boolean found = false;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.getValue(LogInUser.class).getUsername().equals(username)) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    LogInUser logInUser = new LogInUser();
                                    logInUser.setUsername(username);
                                    SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                    FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                }
                            } else {
                                LogInUser logInUser = new LogInUser();
                                logInUser.setUsername(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this));
                                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                            }

                            if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this).equals("administrator")) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("-LhAlUgxE0fJAKBCLl-u");
                                Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            FirebaseDatabase.getInstance().getReference("LogIns").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() == 8) {
                                        SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                        FirebaseDatabase.getInstance().getReference("LogIns").child(startData).removeValue();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                String currentUser = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
//                FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
//                        .equalTo(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("true")) {
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    id = ds.getKey();
//                                    mUser = ds.getValue(User.class);
//                                    Date date = new Date();
//                                    mUser.setLastLoggedIn((-1) * date.getTime());
//                                    FirebaseDatabase.getInstance().getReference("Users/" + id).setValue(mUser);
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this).equals("administrator")) {
//                    Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            } else {
                setContentView(R.layout.activity_log_in);

                initView();
                mUser = new User();

            } }else {
                setContentView(R.layout.progress_layout);
            }
//        firebaseAuth = FirebaseAuth.getInstance();
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
//
//        if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.REMEMBER, LogInActivity.this).equals("True")) {
//            Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            setContentView(R.layout.activity_log_in);
//
//            initView();
//            mUser = new User();
//
//        }
        }

    public void btnLogInOnClick(View view) {

        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if(username.equals("") || TextUtils.isEmpty(username)) {
            mEditTextUsername.setError(getResources().getString(R.string.error_username_empty));
        } else if (password.equals("") || TextUtils.isEmpty(password)) {
            mEditTextPassword.setError(getResources().getString(R.string.error_password_empty));
         } else {
            Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                    .equalTo(username);
            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "false",LogInActivity.this);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Is only one user with this username.
                    if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("false")) {
                        if (!dataSnapshot.hasChildren()) {
                            mEditTextUsername.setError(getResources().getString(R.string.error_username_non_existent));
                        } else {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                mUser = ds.getValue(User.class);
                                String email = mUser.getEmail();
                                // id = ds.getKey();
                                // Date date = new Date();
                                //mUser.setLastLoggedIn((-1) * date.getTime());
                                SharedPreferencesHelper.setStringValueForUserInfo(Constants.EMAIL, email, LogInActivity.this);
                                firebaseAuth.signInWithEmailAndPassword(email, mEditTextPassword.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    if (mCheckBoxRemember.isChecked()) {
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.REMEMBER, "True", LogInActivity.this);
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.USERNAME, mEditTextUsername.getText().toString(), LogInActivity.this);
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.PASS, mEditTextPassword.getText().toString(), LogInActivity.this);
                                                    } else {
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.REMEMBER, "False", LogInActivity.this);
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.USERNAME, mEditTextUsername.getText().toString(), LogInActivity.this);
                                                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.PASS, mEditTextPassword.getText().toString(), LogInActivity.this);
                                                    }
                                                    FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mUser);
                                                    Date date = new Date();
                                                    SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "true", LogInActivity.this);
                                                    FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastLoggedIn").setValue((-1) * date.getTime());
                                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                                                    data = formatter1.format(date);
                                                    Calendar c = Calendar.getInstance();
                                                    c.setTime(date);
                                                    c.add(Calendar.DATE,  -8);
                                                    Date start = c.getTime();
                                                    startData = formatter1.format(start);
                                                    SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "False",LogInActivity.this);
                                                    FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastDate").setValue(formatter.format(date));
                                                    FirebaseDatabase.getInstance().getReference("LogIns").child(data).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("False")) {
                                                                if (dataSnapshot.exists()) {
                                                                    String username = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
                                                                    boolean found = false;
                                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                        if (ds.getValue(LogInUser.class).getUsername().equals(username)) {
                                                                            found = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!found) {
                                                                        LogInUser logInUser = new LogInUser();
                                                                        logInUser.setUsername(username);
                                                                        SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                                        FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                                                    }
                                                                } else {
                                                                    LogInUser logInUser = new LogInUser();
                                                                    logInUser.setUsername(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this));
                                                                    SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                                    FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                                                }

                                                                if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this).equals("administrator")) {
                                                                    Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                FirebaseDatabase.getInstance().getReference("LogIns").addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if(dataSnapshot.getChildrenCount() == 8) {
                                                                            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                                            FirebaseDatabase.getInstance().getReference("LogIns").child(startData).removeValue();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
//                                                    if (mEditTextUsername.getText().toString().equals("administrator")) {
//                                                        Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    } else {
//                                                        Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
                                                } else {
                                                    mEditTextPassword.setError(getResources().getString(R.string.error_password_incorrect));
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void btnSignInOnClick(View view) {
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void btnSignInGoogleOnClick() {

        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, Constants.RC_SIGN_IN_GOOGLE);
    }

    public void initView() {
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogIn);
        setSupportActionBar(toolbar);

        mEditTextUsername = (EditText) findViewById(R.id.etUsername);
        mEditTextPassword = (EditText) findViewById(R.id.etPassword);
        mCheckBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
        signInButton = (SignInButton) findViewById(R.id.btnSignInGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignInGoogleOnClick();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthenticationWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void firebaseAuthenticationWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    for(final Account account: list)
                    {
                        if(account.type.equalsIgnoreCase("com.google"))
                        {
                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.EMAIL, account.name, getApplicationContext());
                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.USERNAME, account.name, getApplicationContext());
                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.REMEMBER, "True", getApplicationContext());
                            Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                                    .equalTo(account.name);
                            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "false",LogInActivity.this);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Is only one user with this username.
                                    if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("false")) {
                                        if (!dataSnapshot.hasChildren()) {
                                            User user = new User();
                                            user.setEmail(account.name);
                                            user.setUsername(account.name);
//                                            Date date = new Date();
//                                            user.setLastLoggedIn((-1) * date.getTime());
//                                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                                            user.setLastDate(formatter.format(date));
                                            FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                                        } else {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                mUser = ds.getValue(User.class);
                                                FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mUser);
                                            }
                                        }
                                          Date date = new Date();
                                          SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "true", LogInActivity.this);
                                          FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastLoggedIn").setValue((-1) * date.getTime());
                                          SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                                          data = formatter1.format(date);
                                          Calendar c = Calendar.getInstance();
                                          c.setTime(date);
                                          c.add(Calendar.DATE,  -8);
                                          Date start = c.getTime();
                                          startData = formatter1.format(start);
                                          SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "False",LogInActivity.this);
                                          FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid() + "/lastDate").setValue(formatter.format(date));
                                          FirebaseDatabase.getInstance().getReference("LogIns").child(data).addValueEventListener(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", LogInActivity.this).equals("False")) {
                                                        if (dataSnapshot.exists()) {
                                                            String username = SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this);
                                                            boolean found = false;
                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                if (ds.getValue(LogInUser.class).getUsername().equals(username)) {
                                                                    found = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (!found) {
                                                                LogInUser logInUser = new LogInUser();
                                                                logInUser.setUsername(username);
                                                                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                                FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                                            }
                                                        } else {
                                                            LogInUser logInUser = new LogInUser();
                                                            logInUser.setUsername(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, LogInActivity.this));
                                                            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                            FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);
                                                        }

                                                        Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                        FirebaseDatabase.getInstance().getReference("LogIns").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.getChildrenCount() == 8) {
                                                                    SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", LogInActivity.this);
                                                                    FirebaseDatabase.getInstance().getReference("LogIns").child(startData).removeValue();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//                                                    if (mEditTextUsername.getText().toString().equals("administrator")) {
//                                                        Intent intent = new Intent(LogInActivity.this, AdministratorActivity.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    } else {
//                                                        Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                    }
//                    Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
//                    startActivity(intent);
//                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
