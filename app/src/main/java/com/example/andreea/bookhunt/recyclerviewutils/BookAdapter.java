package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private Context context;
    private ArrayList<Book> books;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BookViewHolder(LayoutInflater.from(context).inflate(R.layout.book_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        bookViewHolder.mTextViewBookAuthor.setText(books.get(i).getAuthor());
        bookViewHolder.mTextViewBookTitle.setText(books.get(i).getBookTitle());
        Picasso.get().load(books.get(i).getPhotoUrl()).into(bookViewHolder.mImageViewBookPhoto);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
