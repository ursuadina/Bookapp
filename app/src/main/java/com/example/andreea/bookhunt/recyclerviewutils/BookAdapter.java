package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andreea.bookhunt.BHResultActivity;
import com.example.andreea.bookhunt.OptionsActivity;
import com.example.andreea.bookhunt.PopupWindowActivity;
import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.ResultActivity;
import com.example.andreea.bookhunt.ResultsIDreamBooksActivity;
import com.example.andreea.bookhunt.ReviewActivity;
import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.retrofitUtils.IDreamBooksAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.IDreamBooksResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.criticReview.CriticReview;
import com.example.andreea.bookhunt.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private Context context;
    private ArrayList<Book> books;

//    private String title;
//    private String photoUrl;
//    private String description;
////    private Book book;
//    private String originalBookId;
//    private String author;

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
    public void onBindViewHolder(@NonNull final BookViewHolder bookViewHolder, int i) {
        final Book book = books.get(i);
        final String title = book.getBookTitle();
        final String photoUrl = book.getPhotoUrl();
        final String description = book.getDescription();
        final String originalBookId = book.getOriginalBookId();
        final String author = book.getAuthor();

        bookViewHolder.mTextViewBookAuthor.setText(books.get(i).getAuthor());
        bookViewHolder.mTextViewBookTitle.setText(books.get(i).getBookTitle());
        Picasso.get().load(books.get(i).getPhotoUrl()).into(bookViewHolder.mImageViewBookPhoto);
        bookViewHolder.mRatingBarGoodReads.setRating(books.get(i).getRating());
        bookViewHolder.mRatingBarGoodReads.setStepSize((float) 0.5);

        if (book.isFav()) {
            bookViewHolder.mImageButtonAddFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            bookViewHolder.mImageButtonAddFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        bookViewHolder.mImageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book.getBookId(), book.getPhotoUrl(), book.getOriginalBookId());
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
        bookViewHolder.mImageButtonAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReviewBook(book.getOriginalBookId(), book.getBookTitle(), book.getAuthor());
            }
        });
        bookViewHolder.mImageButtonViewGoodreads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewGoodreadsReview(book.getBookId());//book.getBookTitle(), book.getAuthor(), book.getPhotoUrl());
            }
        });
        bookViewHolder.mImageButtonViewIDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewIDBReview(book.getBookTitle(), book.getAuthor());
            }
        });

        bookViewHolder.mImageButtonAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav(bookViewHolder.mImageButtonAddFav, book);
            }
        });

        bookViewHolder.mImageButtonBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BHResultActivity.class);
                intent.putExtra(Constants.PHOTO_URL, photoUrl);
                intent.putExtra(Constants.TITLE, title);
                intent.putExtra(Constants.AUTHOR, author);
                intent.putExtra(Constants.BOOK_ID, originalBookId);
                context.startActivity(intent);
            }
        });
    }

    private void addToFav(ImageButton mImageButtonAddFav, Book book) {
        if(!book.isFav()) {//(Integer) mImageButtonAddFav.getTag() == R.drawable.ic_favorite_border_black_24dp) {
            mImageButtonAddFav.setImageResource(R.drawable.ic_favorite_black_24dp);
            //mImageButtonAddFav.setTag(R.drawable.ic_favorite_black_24dp);
            book.setFav(true);

//            DatabaseReference databaseFavourite = FirebaseDatabase.getInstance().getReference("Favourite");
//            databaseFavourite.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(book.getBookId()).setValue(book);

            DatabaseReference databaseBooks =  FirebaseDatabase.getInstance().getReference("Books");
            databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(book.getBookId()).setValue(book);
        } else {
            mImageButtonAddFav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            //mImageButtonAddFav.setTag(R.drawable.ic_favorite_border_black_24dp);

            book.setFav(false);
//


            DatabaseReference databaseBooks =  FirebaseDatabase.getInstance().getReference("Books");
            databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(book.getBookId()).setValue(book);
        }
    }

    private void viewIDBReview(final String bookTitle, final String author) {
//        Intent intent = new Intent(context, ResultsIDreamBooksActivity.class);
//        intent.putExtra(Constants.TITLE, bookTitle);
//        intent.putExtra(Constants.AUTHOR, author);
//        intent.putExtra(Constants.PHOTO_URL, photoUrl);
//        context.startActivity(intent);
        Retrofit retrofitIDB = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_IDREAMBOOKS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        IDreamBooksAPI iDreamBooksAPI = retrofitIDB.create(IDreamBooksAPI.class);
        String titleAuthor1 = bookTitle + " " + author;

        Call<IDreamBooksResponse> callIDB = iDreamBooksAPI.getIDreamBooksResponse(titleAuthor1, Constants.DEVELOPER_KEY_IDREAMBOOKS);
        callIDB.enqueue(new Callback<IDreamBooksResponse>() {
            @Override
            public void onResponse(Call<IDreamBooksResponse> call, Response<IDreamBooksResponse> response) {
                ArrayList<ResultIDB> resultIDBArrayList = new ArrayList<>();
                List<CriticReview> criticReviews = response.body().getBookIDB().getCriticReviews().getCriticReview();
                for(int i = 0; i < criticReviews.size(); i++) {
                    String mSnippet = criticReviews.get(i).getSnippet();
                    String mSource = criticReviews.get(i).getSource();
                    float mReview = criticReviews.get(i).getStarRating();
                    ResultIDB mResultIDB = new ResultIDB(mSnippet, mSource, mReview);
                    resultIDBArrayList.add(mResultIDB);
                }
                Intent intent = new Intent(context, ResultsIDreamBooksActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.RESULT_ARRAY_IDB, resultIDBArrayList);
                bundle.putString(Constants.TITLE, bookTitle);
                bundle.putString(Constants.AUTHOR, author);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }


            @Override
            public void onFailure(Call<IDreamBooksResponse> call, Throwable t) {
                Log.e("ResultsIDreamBooks", "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewGoodreadsReview(String bookId) { //String bookTitle, String author, String photoUrl) {

        Query query = FirebaseDatabase.getInstance().getReference("Books/" + FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderByChild("bookId").equalTo(bookId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book currentBook = ds.getValue(Book.class);
                    String review_widget = currentBook.getReview_widget();
                    String photoUrl = currentBook.getPhotoUrl();
                    float average_rating = currentBook.getRating();
                    String bookTitle = currentBook.getBookTitle();
                    String author = currentBook.getAuthor();

                    Intent intent = new Intent(context, ResultActivity.class);
                    intent.putExtra(Constants.PHOTO_URL, photoUrl);
                    intent.putExtra(Constants.REVIEW_WIDGET, review_widget);
                    intent.putExtra(Constants.AVERAGE_RATING, average_rating);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addReviewBook(String bookId, String bookTitle, String author) {
        //TODO: add book review + rating
        //Toast.makeText(context, "TODO: add book review", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ReviewActivity.class);
        intent.putExtra(Constants.BOOK_ID, bookId);
        intent.putExtra(Constants.TITLE, bookTitle);
        intent.putExtra(Constants.AUTHOR, author);
        context.startActivity(intent);
    }

    public void deleteBook(String id, String photoUrl, String originalBookId) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(originalBookId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference("Books")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(id);
        databaseReference.removeValue();

        DatabaseReference databaseFavourite = FirebaseDatabase.getInstance()
                .getReference("Favourite")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(id);
        databaseFavourite.removeValue();

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
