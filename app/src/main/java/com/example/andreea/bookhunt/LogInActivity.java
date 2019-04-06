package com.example.andreea.bookhunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private CheckBox mCheckBoxRemember;

    private FirebaseAuth firebaseAuth;

    private Register mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.REMEMBER, LogInActivity.this).equals("True")) {
            String email = SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, LogInActivity.this);
            String pass = SharedPreferencesHelper.getStringValueForUserInfo(Constants.PASS, LogInActivity.this);
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            intent.putExtra(Constants.MESSAGE, "buna din telefon");
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_log_in);

            initView();

            mRegister = new Register();
        }
    }

//    private boolean checkSharedPreferences() {
//    }
    //TODO: log in
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
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        mRegister = ds.getValue(Register.class);
                        String email = mRegister.getEmail();
                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.EMAIL, email, LogInActivity.this);
                        firebaseAuth.signInWithEmailAndPassword(email,mEditTextPassword.getText().toString())
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
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    intent.putExtra(Constants.MESSAGE, "buna din pagina de login");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String errorMessage = task.getException().toString();
                                    Toast.makeText(LogInActivity.this, "Error " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Toast.makeText(LogInActivity.this, mRegister.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

//        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
//        //save username and password in shared preferences so that the user will not have to login every time
//        mPreferences = getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
//        mEditor = mPreferences.edit();
//        mEditor.putString(Constants.USERNAME, mEditTextUsername.getText().toString());
//        mEditor.putString(Constants.PASS, mEditTextPassword.getText().toString());
//        if (mCheckBoxRemember.isChecked()) {
//            mEditor.putString(Constants.REMEMBER, "True");
//        } else {
//            mEditor.putString(Constants.REMEMBER, "False");
//        }
//        mEditor.apply();
    }

    //TODO: sign in
    public void btnSignInOnClick(View view) {
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        intent.putExtra(Constants.MESSAGE, "buna din pagina de login");
        startActivity(intent);
    }
    //TODO: log in with google
    public void btnSignInGoogleOnClick(View view) {
        Toast.makeText(LogInActivity.this, "TO DO: Log in with Google on click",
                Toast.LENGTH_SHORT).show();
    }

    public void initView() {
        mEditTextUsername = (EditText) findViewById(R.id.etUsername);
        mEditTextPassword = (EditText) findViewById(R.id.etPassword);
        mCheckBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
    }
}
