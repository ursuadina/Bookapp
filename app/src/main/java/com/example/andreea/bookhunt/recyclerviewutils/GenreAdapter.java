package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.Genre;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder> {

    private Context context;
    private ArrayList<Genre> genres;

    public GenreAdapter(Context context, ArrayList<Genre> genres) {
        this.context = context;
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GenreViewHolder(LayoutInflater.from(context).inflate(R.layout.genre_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int i) {
        final Genre genre = genres.get(i);
        genreViewHolder.tvGenre.setText(genre.getGenre());

        genreViewHolder.mImageButtonDeleteGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGenre(genre.getId());
            }
        });
    }

    private void deleteGenre(String id) {
        FirebaseDatabase.getInstance().getReference("Genres").child(id).removeValue();
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}
