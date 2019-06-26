package com.example.andreea.bookhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.LogInUser;
import com.example.andreea.bookhunt.models.User;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.EmailHelper;
import com.example.andreea.bookhunt.utils.NameHelper;
import com.example.andreea.bookhunt.utils.PasswordHelper;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.example.andreea.bookhunt.utils.UsernameHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private EditText mEditTextEmail;
    private TextView mTextView;
    private ProgressDialog mProgressDialog;

    private User mUser;

    private FirebaseAuth firebaseAuth;
    private String data;
    private String startData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();

        mUser = new User();
        mProgressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        initView();
    }

    public void initView() {
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);

        mEditTextEmail = (EditText) findViewById(R.id.etEmail);
        mEditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmailValid();
            }
        });

        mEditTextFirstName = (EditText) findViewById(R.id.etFirstName);
        mEditTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isFirstNameValid();
            }
        });

        mEditTextLastName = (EditText) findViewById(R.id.etLastName);
        mEditTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isLastNameValid();
            }
        });

        mEditTextUsername = (EditText) findViewById(R.id.etUsernameReg);
        mEditTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isUsernameValid();
            }
        });

        mEditTextPassword = (EditText) findViewById(R.id.etPasswordReg);
        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password1 = s.toString();
                String username = mEditTextUsername.getText().toString();

                if (PasswordHelper.isPasswordEqUsername(password1, username)) {
                    mEditTextPassword.setError(getResources().getString(R.string.error_password_eq_username_input));
                } else if(password1.length() < 7 ) {
                    mEditTextPassword.setError(getResources().getString(R.string.error_password_input));
                }
            }
        });

        mEditTextConfirmPassword = (EditText) findViewById(R.id.etConfPasswordReg);
        mEditTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isPasswordValid();
            }
        });

    }

    private boolean isEmailValid() {
        String email = mEditTextEmail.getText().toString();
        if (EmailHelper.isEmailValid(email)) {
            
            mUser.setEmail(email);
            return true;
        } else {
            mEditTextEmail.setError(getResources().getString(R.string.error_email_input));
            return false;
        }
    }

    private boolean isFirstNameValid() {
        String firstName = mEditTextFirstName.getText().toString();
        if (NameHelper.isNameValid(firstName)) {
            mUser.setFirstName(firstName);
            return true;
        } else {
            mEditTextFirstName.setError(getResources().getString(R.string.error_name_input));
            return false;
        }
    }

    private boolean isLastNameValid() {
        String lastName = mEditTextLastName.getText().toString();
        if (NameHelper.isNameValid(lastName)) {
            mUser.setLastName(lastName);
            return true;
        } else {
            mEditTextLastName.setError(getResources().getString(R.string.error_name_input));
            return false;
        }
    }

    private boolean isPasswordValid() {
        String password1 = mEditTextPassword.getText().toString();
        String password2 = mEditTextConfirmPassword.getText().toString();
        if (PasswordHelper.isPasswordValid(password1)) {
            if (PasswordHelper.isConfirmationPassValid(password1, password2)) {
                return true;
            } else {
                mEditTextConfirmPassword.setError(getResources().getString(R.string.error_conf_password_input));
                return false;
            }
        } else {
            mEditTextPassword.setError(getResources().getString(R.string.error_password_input));
            return false;
        }
    }

    private boolean isUsernameValid() {
        String username = mEditTextUsername.getText().toString();
        if (UsernameHelper.isUsernameValid(username)) {
            mUser.setUsername(username);
            return true;
        } else {
            mEditTextUsername.setError(getResources().getString(R.string.error_username_input));
            return false;
        }
    }

    //TODO: register
    public void btnRegisterOnClick(View view) {
        if (!isFirstNameValid() | !isLastNameValid() | !isUsernameValid() | !isPasswordValid() | !isEmailValid()) {
            return;
        } else {
            String username = mEditTextUsername.getText().toString();
            Date date = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
            data = formatter1.format(date);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, -7);
            Date start = c.getTime();
            startData = formatter1.format(start);
            mUser.setLastLoggedIn(date.getTime() * (-1));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            mUser.setLastDate(formatter.format(date));
            Query query1 = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                    .equalTo(username);
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   mEditTextUsername.setError(getResources().getString(R.string.error_username_exists));
                   return;
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
        }
        final String email = mUser.getEmail();
        final String password = mEditTextPassword.getText().toString();

        mProgressDialog.setMessage("Registering...");
        mProgressDialog.show();

//        The user is created and I save his informations(first name, last name, etc.) also in the
//        database.

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseAuth.signInWithEmailAndPassword(mUser.getEmail(),
                                            mEditTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()) {
                                                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "False",RegisterActivity.this);
                                                FirebaseDatabase.getInstance().getReference("LogIns").child(data).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (SharedPreferencesHelper.getStringValueForUserInfo("ModifyLogIn", RegisterActivity.this).equals("False")) {
                                                            if (dataSnapshot.exists()) {
                                                                String username = mEditTextUsername.getText().toString();
                                                                LogInUser logInUser = new LogInUser();
                                                                logInUser.setUsername(username);
                                                                SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", RegisterActivity.this);
                                                                FirebaseDatabase.getInstance().getReference("LogIns/" + data).push().setValue(logInUser);

                                                                Intent intent = new Intent(RegisterActivity.this, IndexActivity.class);
                                                                startActivity(intent);
                                                                mProgressDialog.dismiss();
                                                                finish();
                                                                FirebaseDatabase.getInstance().getReference("LogIns").addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.getChildrenCount() == 8) {
                                                                            SharedPreferencesHelper.setStringValueForUserInfo("ModifyLogIn", "True", RegisterActivity.this);
                                                                            FirebaseDatabase.getInstance().getReference("LogIns").child(startData).removeValue();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                                else {
                                                String errorMessage = task.getException().toString();
                                                Toast.makeText(RegisterActivity.this, "Error " + errorMessage, Toast.LENGTH_SHORT).show();
                                                mProgressDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                mEditTextEmail.setError(getResources().getString(R.string.error_email_exists));
                                mProgressDialog.dismiss();
                            } catch (Exception e) {
                                String errorMessage = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error " + errorMessage, Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                    }
                });
    }
}
