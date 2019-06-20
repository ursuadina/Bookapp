package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class GenreViewHolder  extends RecyclerView.ViewHolder {
    public TextView tvGenre;
    public ImageButton mImageButtonDeleteGenre;


    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        tvGenre = (TextView) itemView.findViewById(R.id.textViewGenre);
        mImageButtonDeleteGenre = (ImageButton) itemView.findViewById(R.id.imageViewDeleteGenre);
    }
}