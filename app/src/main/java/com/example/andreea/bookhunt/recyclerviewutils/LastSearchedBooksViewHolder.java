package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class LastSearchedBooksViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitleAuthor;
    public TextView tvLastSearched;
    public TextView tvUsername;
    public LastSearchedBooksViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        tvTitleAuthor = (TextView) itemView.findViewById(R.id.textViewTitleAuthor);
        tvLastSearched = (TextView) itemView.findViewById(R.id.textViewLastSearched);
        tvUsername = (TextView) itemView.findViewById(R.id.textViewUsernameLSB);

    }
}
