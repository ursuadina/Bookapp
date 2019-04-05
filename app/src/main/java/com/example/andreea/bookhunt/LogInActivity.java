package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.andreea.bookhunt.utils.Constants;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //todo: user deja autentificat
    }

    //TODO: log in
    public void btnLogInOnClick(View view) {
        //Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        Toast.makeText(LogInActivity.this, "TO DO: Log in on click", Toast.LENGTH_SHORT)
                .show();
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
}
