package com.example.andreea.bookhunt;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.Genre;
import com.example.andreea.bookhunt.models.LastSearchBook;
import com.example.andreea.bookhunt.recyclerviewutils.GenreAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewGenresActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewGenre;

    private ArrayList<Genre> genres;
    private GenreAdapter genreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_genres);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        initView();

        genres = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Genres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    genres.clear();
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        Genre genre = ds.getValue(Genre.class);
                        genres.add(genre);
                    }
                    genreAdapter = new GenreAdapter(ViewGenresActivity.this, genres);
                    mRecyclerViewGenre.setAdapter(genreAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarViewGenre);
        setSupportActionBar(toolbar);

        mRecyclerViewGenre = findViewById(R.id.rvGenres);
        mRecyclerViewGenre.setLayoutManager(new LinearLayoutManager(this));

    }
}
