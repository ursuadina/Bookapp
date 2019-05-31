package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andreea.bookhunt.PopupWindowActivity;
import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private Context context;
    private ArrayList<Book> books;

    private String title;
    private String photoUrl;
    private String description;
    private Book book;

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
        book = books.get(i);
        title = book.getBookTitle();
        photoUrl = book.getPhotoUrl();
        description = book.getDescription();

        bookViewHolder.mTextViewBookAuthor.setText(books.get(i).getAuthor());
        bookViewHolder.mTextViewBookTitle.setText(books.get(i).getBookTitle());
        Picasso.get().load(books.get(i).getPhotoUrl()).into(bookViewHolder.mImageViewBookPhoto);
        bookViewHolder.mRatingBarGoodReads.setRating(books.get(i).getRating());
        bookViewHolder.mRatingBarGoodReads.setStepSize((float) 0.5);
        bookViewHolder.mImageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book.getBookId(), book.getPhotoUrl());
            }
        });
        bookViewHolder.mImageButtonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PopupWindowActivity.class);
                intent.putExtra(Constants.PHOTO_URL, photoUrl);
                intent.putExtra(Constants.TITLE, title);
                intent.putExtra(Constants.DESCRIPTION, description);
                context.startActivity(intent);
            }
        });
    }

    public void deleteBook(String id, String photoUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(id);
        databaseReference.removeValue();

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Element deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return books.size();
    }

}
