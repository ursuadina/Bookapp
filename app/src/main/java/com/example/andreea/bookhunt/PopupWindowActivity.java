package com.example.andreea.bookhunt;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreea.bookhunt.utils.Constants;
import com.squareup.picasso.Picasso;

public class PopupWindowActivity extends AppCompatActivity {
    private String photoUrl;
    private String title;
    private String description;

    private ImageView imageViewPopup;
    private Toolbar toolbar;
    private TextView textViewDescription;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        initView();

        imageViewPopup.getLayoutParams().height = (int) ((width * 0.6) * 1.5);
        collapsingToolbarLayout.getLayoutParams().height = (int) ((width * 0.6) * 1.5);
        Intent intent = getIntent();

        photoUrl = intent.getStringExtra(Constants.PHOTO_URL);
        title = intent.getStringExtra(Constants.TITLE);
        description = intent.getStringExtra(Constants.DESCRIPTION);

        getSupportActionBar().setTitle(title);
        Picasso.get().load(photoUrl).into(imageViewPopup);
        textViewDescription.setText(description);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbarPopup);
        setSupportActionBar(toolbar);

        imageViewPopup = (ImageView) findViewById(R.id.imageViewPopup);
        textViewDescription = (TextView) findViewById(R.id.textViewDescription);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

    }
}
