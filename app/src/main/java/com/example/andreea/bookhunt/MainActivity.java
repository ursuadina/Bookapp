package com.example.andreea.bookhunt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.andreea.bookhunt.utils.Constants;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        Intent intent = getIntent();
        mPreferences = getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
        String username;
        String pass;
        if (intent != null) {
            username = mPreferences.getString(Constants.USERNAME, "");
            pass = mPreferences.getString(Constants.PASS,"");
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(username + " " + pass);
        }
    }

    public void initView() {
        mTextView = (TextView) findViewById(R.id.textView2);
    }
}
