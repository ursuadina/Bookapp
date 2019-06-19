package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.LastSearchBook;
import com.example.andreea.bookhunt.models.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LastSearchedBooksAdapter extends RecyclerView.Adapter<LastSearchedBooksViewHolder>{

    private Context context;
    private ArrayList<LastSearchBook> books;

    private String title;
    private String author;
    private String lastSearch;
    private String username;
    private LastSearchBook book;

    public LastSearchedBooksAdapter(Context context, ArrayList<LastSearchBook> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public LastSearchedBooksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LastSearchedBooksViewHolder(LayoutInflater.from(context).inflate(R.layout.last_book_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LastSearchedBooksViewHolder lastSearchedBooksViewHolder, int i) {
        book = books.get(i);
        username = book.getUsername();
        title = book.getTitle();
        author = book.getAuthor();
        lastSearch = book.getLastSearched();

        String titleAuthor = title + " by " + author;

        lastSearchedBooksViewHolder.tvTitleAuthor.setText(titleAuthor);
        lastSearchedBooksViewHolder.tvUsername.setText(username);
        lastSearchedBooksViewHolder.tvLastSearched.setText(lastSearch);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
