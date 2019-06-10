package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    private Button mButtonGoodReads;
    private Button mButtonNYT;
    private Bundle bundleExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent intent = getIntent();
        bundleExtras = intent.getExtras();
        initView();
    }

    private void initView() {
        mButtonGoodReads = (Button) findViewById(R.id.buttonGoodReads);
        mButtonNYT = (Button) findViewById(R.id.buttonNYT);
    }

    public void btnGoodReadsResult(View view) {
        Intent intent = new Intent(OptionsActivity.this, ResultActivity.class);
        intent.putExtras(bundleExtras);
        startActivity(intent);
    }

    public void btnIDBResults(View view) {
        Intent intent = new Intent(OptionsActivity.this, ResultsIDreamBooksActivity.class);
        intent.putExtras(bundleExtras);
        startActivity(intent);
    }

    public void btnBHResult(View view) {
        Intent intent = new Intent(OptionsActivity.this, BHResultActivity.class);
        startActivity(intent);
    }
}
