package com.example.andreea.bookhunt;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import com.example.andreea.bookhunt.models.Genre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddGenreActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutGenre;
    private EditText editTextGenre;

    private String genreToAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_genre);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGenre);
        setSupportActionBar(toolbar);
        textInputLayoutGenre = (TextInputLayout) findViewById(R.id.textInputLayoutGenre);
        editTextGenre = (EditText) findViewById(R.id.etGenre);
    }

    public void btnAdd(View view) {
        genreToAdd = editTextGenre.getText().toString();
        FirebaseDatabase.getInstance().getReference("Genres").orderByChild("genre").equalTo(genreToAdd).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    editTextGenre.setError("Genre exists");
                } else {
                    String id = FirebaseDatabase.getInstance().getReference("Genres").push().getKey();
                    Genre genre = new Genre(id, genreToAdd);
                    FirebaseDatabase.getInstance().getReference("Genres").child(id).setValue(genre);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
