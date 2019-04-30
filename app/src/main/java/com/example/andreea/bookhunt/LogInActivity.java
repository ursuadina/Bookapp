package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Register;
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

public class LogInActivity extends AppCompatActivity{

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private CheckBox mCheckBoxRemember;
    private SignInButton signInButton;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private Register mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.REMEMBER, LogInActivity.this).equals("True")) {
            Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_log_in);

            initView();
            mRegister = new Register();

        }
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
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Is only one user with this username.
                    if (!dataSnapshot.hasChildren()){
                        mEditTextUsername.setError(getResources().getString(R.string.error_username_non_existent));
                    } else {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mRegister = ds.getValue(Register.class);
                            String email = mRegister.getEmail();
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
                                                Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                mEditTextPassword.setError(getResources().getString(R.string.error_password_incorrect));
                                            }
                                        }
                                    });
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
        intent.putExtra(Constants.MESSAGE, "buna din pagina de login");
        startActivity(intent);
    }

    public void btnSignInGoogleOnClick() {

        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, Constants.RC_SIGN_IN_GOOGLE);
    }

    public void initView() {
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
                    Intent intent = new Intent(LogInActivity.this, IndexActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
