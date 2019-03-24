package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private EditText mEditTextEmail;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        if (intent != null) {
            Toast.makeText(RegisterActivity.this, intent.getStringExtra(MainActivity.MESSAGE),
                    Toast.LENGTH_SHORT).show();
        }

        initView();
    }

    public void initView(){
        mEditTextEmail = (EditText) findViewById(R.id.etEmail);
        mEditTextFirstName = (EditText) findViewById(R.id.etFirstName);
        mEditTextLastName = (EditText) findViewById(R.id.etLastName);
        mEditTextPassword = (EditText) findViewById(R.id.etPasswordReg);
        mEditTextConfirmPassword = (EditText) findViewById(R.id.etConfPasswordReg);
        mEditTextUsername = (EditText) findViewById(R.id.etUsernameReg);

        mTextView = (TextView) findViewById(R.id.textView);
    }

    //TODO: register
    public void btnRegisterOnClick(View view) {
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("First name:" + mEditTextFirstName.getText() +'\n' + "Last name: " + mEditTextLastName.getText() +
                '\n' + "Password: " + mEditTextPassword.getText() + '\n' + "Email: " + mEditTextEmail.getText()
                + '\n' + "Username: " + mEditTextUsername.getText());
    }
}
