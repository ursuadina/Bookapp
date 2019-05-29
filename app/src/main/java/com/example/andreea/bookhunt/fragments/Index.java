package com.example.andreea.bookhunt.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.IndexActivity;
import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.SearchActivity;
import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.recyclerviewutils.BookAdapter;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.SharedPreferencesHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class Index extends Fragment {
    private RecyclerView recyclerView;
    private TextView mTextViewUsername;
    private TextView mTextViewEmail;
    private NavigationView mNavigationView;
    private LinearLayout mLinearLayoutHeader;
    private RecyclerView mRecyclerViewBooks;
    private ImageButton mImageButtonDelete;
    private ArrayList<Book> books;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference booksReference;

    private String mCurrentPhotoPath;
    private File photoFile;

    private BookAdapter bookAdapter;

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_index, container, false);

        initView();

        books = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        booksReference = FirebaseDatabase.getInstance().getReference("Books")
                .child(firebaseAuth.getCurrentUser().getUid());

        booksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    books.add(book);
                }
                bookAdapter = new BookAdapter(getContext(), books);
                mRecyclerViewBooks.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    public void initView() {
        mNavigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        mLinearLayoutHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTextViewUsername = (TextView) mLinearLayoutHeader.findViewById(R.id.tvUsername);
        mTextViewUsername.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.USERNAME, getContext()));
        mTextViewEmail = (TextView) mLinearLayoutHeader.findViewById(R.id.tvEmail);
        mTextViewEmail.setText(SharedPreferencesHelper.getStringValueForUserInfo(Constants.EMAIL, getContext()));
        mRecyclerViewBooks = rootView.findViewById(R.id.rvBooks);
        mRecyclerViewBooks.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    public void btnSearchBook(View view) {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
}
