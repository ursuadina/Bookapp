package com.example.andreea.bookhunt;

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
import com.example.andreea.bookhunt.models.OriginalBooks;
import com.example.andreea.bookhunt.models.ResultIDB;
import com.example.andreea.bookhunt.recyclerviewutils.ResultsIDBAdapter;
import com.example.andreea.bookhunt.retrofitUtils.GoodreadsAPI;
import com.example.andreea.bookhunt.retrofitUtils.IDreamBooksAPI;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

    private Book mBook;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;
    private DatabaseReference databaseOriginalBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent intent = getIntent();
        mBookTitle = intent.getStringExtra(Constants.TITLE);
        mAuthor = intent.getStringExtra(Constants.AUTHOR);
        mPhotoUrl = intent.getStringExtra(Constants.PHOTO_URL);

        initView();
        bundleExtras = new Bundle();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseBooks = FirebaseDatabase.getInstance().getReference("Books");
        databaseOriginalBooks = FirebaseDatabase.getInstance().getReference("OriginalBooks");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_GOODREADS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        GoodreadsAPI goodreadsAPI = retrofit.create(GoodreadsAPI.class);
        Call<GoodreadsResponse> call = goodreadsAPI.getGoodreadsResponse(Constants.DEVELOPER_KEY_GOODREADS, mBookTitle, mAuthor);
        call.enqueue(new Callback<GoodreadsResponse>() {
            @Override
            public void onResponse(Call<GoodreadsResponse> call, Response<GoodreadsResponse> response) {
                review_widget = response.body().getBook().getReviews_widget();

                average_rating = response.body().getBook().getAverage_rating();

                bookId = databaseBooks.push().getKey();
                description = response.body().getBook().getDescription().replaceAll("<br />", "\n");

                titleAuthor = mBookTitle + "_" + mAuthor;
                Query query1 = databaseOriginalBooks.orderByChild("titleAuthor").equalTo(titleAuthor);
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            originalBookId = databaseOriginalBooks.push().getKey();
                            OriginalBooks originalBooks = new OriginalBooks(originalBookId, mBookTitle, description, mAuthor, titleAuthor);
                            databaseOriginalBooks.push().setValue(originalBooks);
                        } else {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                originalBookId = ds.getValue(OriginalBooks.class).getBookId();
                            }
                        }
                        if(SharedPreferencesHelper.getStringValueForUserInfo(Constants.IS_CREATED, getApplicationContext()).equals("False")) {
                            mBook = new Book(bookId, mBookTitle, mAuthor, mPhotoUrl, average_rating, description, originalBookId, review_widget);
                            databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(bookId).setValue(mBook);
                            SharedPreferencesHelper.setStringValueForUserInfo(Constants.IS_CREATED, "True", OptionsActivity.this);
                        }
                        bundleExtras.putString(Constants.REVIEW_WIDGET, review_widget);
                        bundleExtras.putString(Constants.PHOTO_URL, mPhotoUrl);
                        bundleExtras.putFloat(Constants.AVERAGE_RATING, average_rating);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(OptionsActivity.this, "nu s-a gasit", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("ResultActivity", "onResponse: GoodReadsReasponse: " + response.body().getBook());
                Log.d("ResultActivity", "onResponse: Server Response: " + response.toString());
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
                resultIDBArrayList = new ArrayList<>();
                List<CriticReview> criticReviews = response.body().getBookIDB().getCriticReviews().getCriticReview();
                for(int i = 0; i < criticReviews.size(); i++) {
                    mSnippet = criticReviews.get(i).getSnippet();
                    mSource = criticReviews.get(i).getSource();
                    mReview = criticReviews.get(i).getStarRating();
                    mResultIDB = new ResultIDB(mSnippet, mSource, mReview);
                    resultIDBArrayList.add(mResultIDB);
                }
                bundleExtras.putParcelableArrayList(Constants.RESULT_ARRAY_IDB, resultIDBArrayList);
            }


            @Override
            public void onFailure(Call<IDreamBooksResponse> call, Throwable t) {
                Log.e("ResultsIDreamBooks", "onFailure: Unable to retrieve RSS: " + t.getMessage());
                Toast.makeText(OptionsActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
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
