package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.ResultIDB;

import java.util.ArrayList;

public class ResultsIDBAdapter extends RecyclerView.Adapter<ResultsIDBViewHolder> {
    private Context context;
    private ArrayList<ResultIDB> resultIDBArrayList;

    private String snippet;
    private String source;
    private float rating;
    private ResultIDB resultIDB;

    public ResultsIDBAdapter(Context context, ArrayList<ResultIDB> resultIDBArrayList) {
        this.context = context;
        this.resultIDBArrayList = resultIDBArrayList;
    }

    @NonNull
    @Override
    public ResultsIDBViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ResultsIDBViewHolder(LayoutInflater.from(context).inflate(R.layout.result_idb_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsIDBViewHolder resultsIDBViewHolder, int i) {
        resultIDB = resultIDBArrayList.get(i);
        snippet = resultIDB.getmSnippet();
        source = resultIDB.getmSource();
        rating = resultIDB.getmRating();

        resultsIDBViewHolder.mTextViewSource.setText(source);
        resultsIDBViewHolder.mTextViewSnippet.setText(snippet);
        resultsIDBViewHolder.mRatingBarReview.setRating(rating);
    }

    @Override
    public int getItemCount() {
        return resultIDBArrayList.size();
    }
}
