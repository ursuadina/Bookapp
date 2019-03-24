package com.example.andreea.bookhunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //TODO: log in
    public void btnLogInOnClick(View view) {
        Toast.makeText(MainActivity.this, "TO DO: Log in on click", Toast.LENGTH_SHORT)
                .show();
    }
    //TODO: sign in
    public void btnSignInOnClick(View view) {
        Toast.makeText(MainActivity.this, "TO DO: Sign in on click", Toast.LENGTH_SHORT)
                        .show();
    }
    //TODO: log in with google
    public void btnSignInGoogleOnClick(View view) {
        Toast.makeText(MainActivity.this, "TO DO: Log in with Google on click",
                Toast.LENGTH_SHORT).show();
    }
}
