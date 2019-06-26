package com.example.andreea.bookhunt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.models.Genre;
import com.example.andreea.bookhunt.models.OriginalBooks;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.recyclerviewutils.ResultsIDBAdapter;
import com.example.andreea.bookhunt.retrofitUtils.GoodreadsAPI;
import com.example.andreea.bookhunt.retrofitUtils.IDreamBooksAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelAuthor.AuthorResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.book.popularShelves.shelf.Shelf;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.IDreamBooksResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.bookIDB.criticReviews.criticReview.CriticReview;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class OptionsActivity extends AppCompatActivity {

    private Button mButtonGoodReads;
    private Button mButtonNYT;
    private Bundle bundleExtras;

    private String bookId;
    private String mBookTitle;
    private String mAuthor;
    private String mPhotoUrl;
    private String review_widget;
    private String originalBookId;
    private String description;
    private String titleAuthor;
    private float average_rating;
    private String mSnippet;
    private float mReview;
    private String mSource;
    private ResultIDB mResultIDB;
    private ArrayList<ResultIDB> resultIDBArrayList;
    private GoodreadsAPI goodreadsAPI;
    private String authorNew;
    private Book mBook;
    private List<Shelf> shelves;
    private ArrayList<String> genres;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;
    private DatabaseReference databaseOriginalBooks;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent intent = getIntent();
        mBookTitle = intent.getStringExtra(Constants.TITLE);
        mAuthor = intent.getStringExtra(Constants.AUTHOR);
        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);
        genres = new ArrayList<>();

        initView();
        bundleExtras = new Bundle();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Books");
        databaseOriginalBooks = FirebaseDatabase.getInstance().getReference("OriginalBooks");

        progressDialog = new ProgressDialog(OptionsActivity.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Retrieving data....");
        progressDialog.setTitle("");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // show it
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_GOODREADS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        goodreadsAPI = retrofit.create(GoodreadsAPI.class);
        Call<GoodreadsResponse> call = goodreadsAPI.getGoodreadsResponse(Constants.DEVELOPER_KEY_GOODREADS, mBookTitle, mAuthor);
        call.enqueue(new Callback<GoodreadsResponse>() {
            @Override
            public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {
                if (response.isSuccessful()) {
                    review_widget = response.body().getBook().getReviews_widget();

                    average_rating = response.body().getBook().getAverage_rating();

                    bookId = databaseBooks.push().getKey();

                    shelves = response.body().getBook().getPopularShelves().getShelf();

                    genres.clear();
                    if (response.body().getBook().getDescription() != null ) {
                        description = response.body().getBook().getDescription().replaceAll("<br />", "\n");
                    } else {
                        description = "There is no description for this book";
                    }
                    titleAuthor = mBookTitle + "_" + mAuthor;
                    Query query1 = databaseOriginalBooks.orderByChild("titleAuthor").equalTo(titleAuthor);
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                originalBookId = databaseOriginalBooks.push().getKey();
                                OriginalBooks originalBooks = new OriginalBooks(originalBookId, mBookTitle, description, mAuthor, titleAuthor);
                                databaseOriginalBooks.child(originalBookId).setValue(originalBooks);
                            } else {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    originalBookId = ds.getValue(OriginalBooks.class).getBookId();
                                }
                            }
                            Query query2 = FirebaseDatabase.getInstance().getReference("Genres");
                            query2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(SharedPreferencesHelper.getStringValueForUserInfo("LastSearch", OptionsActivity.this).equals("False")) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                Genre currentGenre = ds.getValue(Genre.class);
                                                String genre = currentGenre.getGenre();
                                                for (Shelf shelf : shelves) {
                                                    if ((shelf.getName().toLowerCase().contains(genre.toLowerCase()) || genre.toLowerCase().contains(shelf.getName().toLowerCase()))
                                                            && !genres.contains(genre.toLowerCase())) {
                                                        genres.add(genre);
                                                    }
                                                }
                                            }
                                            databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("originalBookId")
                                                    .equalTo(originalBookId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()) {
                                                        if (SharedPreferencesHelper.getStringValueForUserInfo("LastSearch", OptionsActivity.this).equals("False")) {
                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                Book book = ds.getValue(Book.class);
                                                                Date date = new Date();
                                                                SharedPreferencesHelper.setStringValueForUserInfo("LastSearch", "True", OptionsActivity.this);
                                                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                                databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .child(book.getBookId()).child("lastSearchDate").setValue(formatter.format(date));
                                                                databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                        .child(book.getBookId()).child("lastSearch").setValue(date.getTime() * (-1));
                                                            }
                                                        }
                                                    } else {
                                                        if (SharedPreferencesHelper.getStringValueForUserInfo("LastSearch", OptionsActivity.this).equals("False")) {
                                                            Date date = new Date();
                                                            SharedPreferencesHelper.setStringValueForUserInfo("LastSearch", "True", OptionsActivity.this);
                                                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                            mBook = new Book(bookId, mBookTitle, mAuthor, mPhotoUrl, average_rating, description, originalBookId, review_widget, false, date.getTime() * (-1), formatter.format(date), genres);
                                                            mBook.setUsername(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, OptionsActivity.this));
                                                            databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .child(bookId).setValue(mBook);
                                                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.IS_CREATED, "True", OptionsActivity.this);
                                                            FirebaseMessaging.getInstance().subscribeToTopic(originalBookId);
                                                        }
                                                    }
                                                    bundleExtras.putString(Constants.REVIEW_WIDGET, review_widget);
                                                    bundleExtras.putString(Constants.PHOTO_URL, mPhotoUrl);
                                                    bundleExtras.putFloat(Constants.AVERAGE_RATING, average_rating);
                                                    bundleExtras.putString(Constants.BOOK_ID, originalBookId);
                                                    bundleExtras.putString(Constants.TITLE, mBookTitle);
                                                    bundleExtras.putString(Constants.AUTHOR, mAuthor);
                                                    SharedPreferencesHelper.setStringValueForUserInfo(Constants.DONE_GOODREADS, "done", OptionsActivity.this);
                                                    mButtonGoodReads.setEnabled(true);
                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
//                            if (SharedPreferencesHelper.getStringValueForUserInfo(Constants.IS_CREATED, getApplicationContext()).equals("False")) {
//                                mBook = new Book(bookId, mBookTitle, mAuthor, mPhotoUrl, average_rating, description, originalBookId, review_widget, false);
//                                databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        .child(bookId).setValue(mBook);
//                                SharedPreferencesHelper.setStringValueForUserInfo(Constants.IS_CREATED, "True", OptionsActivity.this);
//                            }
//                            bundleExtras.putString(Constants.REVIEW_WIDGET, review_widget);
//                            bundleExtras.putString(Constants.PHOTO_URL, mPhotoUrl);
//                            bundleExtras.putFloat(Constants.AVERAGE_RATING, average_rating);
//                            bundleExtras.putString(Constants.BOOK_ID, originalBookId);
//                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.DONE_GOODREADS, "done", OptionsActivity.this);
//                            mButtonGoodReads.setEnabled(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(OptionsActivity.this, "nu s-a gasit", Toast.LENGTH_SHORT).show();
                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.DONE_GOODREADS, "done", OptionsActivity.this);
                        }
                    });

                    SharedPreferencesHelper.setStringValueForUserInfo(Constants.DONE_GOODREADS, "done", OptionsActivity.this);
                    Log.d("ResultActivity", "onResponse: GoodReadsReasponse: " + response.body().getBook());
                    Log.d("ResultActivity", "onResponse: Server Response: " + response.toString());
                } else {
                    Retrofit retrofit1 = new Retrofit.Builder()
                            .baseUrl(Constants.BASE_URL_GOODREADS)
                            .addConverterFactory(SimpleXmlConverterFactory.create())
                            .build();
                    Call<AuthorResponse> call1 = goodreadsAPI.getAuthorResponse(mAuthor, Constants.DEVELOPER_KEY_GOODREADS);
                    call1.enqueue(new Callback<AuthorResponse>() {
                        @Override
                        public void onResponse(Call<AuthorResponse> call, Response<AuthorResponse> response) {
                            if(response.isSuccessful()) {
                                authorNew = response.body().getAuthor().getName();
                                new AlertDialog.Builder(OptionsActivity.this)
                                        .setTitle("Wrong author")
                                        .setMessage("Did you want to write " + authorNew+ "?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent1 = new Intent(OptionsActivity.this, OptionsActivity.class);
                                                intent1.putExtra(Constants.TITLE, mBookTitle);
                                                intent1.putExtra(Constants.AUTHOR, authorNew);
                                                intent1.putExtra(Constants.PHOTO_URL, mPhotoUrl);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                mButtonGoodReads.setEnabled(false);
                                mButtonGoodReads.setBackground(getDrawable(R.drawable.rounded_button_disabled));
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthorResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GoodreadsResponse> call, Throwable t) {
                Log.e("ResultActivity", "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(OptionsActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        });


        //----------------------------------------------------------
        Retrofit retrofitIDB = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_IDREAMBOOKS)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();
        IDreamBooksAPI iDreamBooksAPI = retrofitIDB.create(IDreamBooksAPI.class);
        String titleAuthor1 = mBookTitle + " " + mAuthor;
        Call<IDreamBooksResponse> callIDB = iDreamBooksAPI.getIDreamBooksResponse(titleAuthor1, Constants.DEVELOPER_KEY_IDREAMBOOKS);
        callIDB.enqueue(new Callback<IDreamBooksResponse>() {
            @Override
            public void onResponse(Call<IDreamBooksResponse> call, Response<IDreamBooksResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getTotalResults() == 0) {
                        mButtonNYT.setEnabled(false);
                        mButtonNYT.setBackground(getDrawable(R.drawable.rounded_button_disabled));
                        progressDialog.dismiss();
                    } else {
                        resultIDBArrayList = new ArrayList<>();
                        List<CriticReview> criticReviews = response.body().getBookIDB().getCriticReviews().getCriticReview();
                        for (int i = 0; i < criticReviews.size(); i++) {
                            mSnippet = criticReviews.get(i).getSnippet();
                            mSource = criticReviews.get(i).getSource();
                            mReview = criticReviews.get(i).getStarRating();
                            mResultIDB = new ResultIDB(mSnippet, mSource, mReview);
                            resultIDBArrayList.add(mResultIDB);
                        }
                        bundleExtras.putParcelableArrayList(Constants.RESULT_ARRAY_IDB, resultIDBArrayList);
                        SharedPreferencesHelper.setStringValueForUserInfo(Constants.DISABLE_IDB, "false", OptionsActivity.this);
                        mButtonNYT.setEnabled(true);
                        progressDialog.dismiss();
                    }
                } else {
                    mButtonNYT.setEnabled(false);
                    mButtonNYT.setBackground(getDrawable(R.drawable.rounded_button_disabled));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<IDreamBooksResponse> call, Throwable t) {
                Log.e("ResultsIDreamBooks", "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(OptionsActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


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
        intent.putExtras(bundleExtras);
        startActivity(intent);
    }
}
