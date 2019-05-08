package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class BookViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageViewBookPhoto;
    public TextView mTextViewBookTitle;
    public TextView mTextViewBookAuthor;


    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);

    }

    public void initView(View itemView) {
        mImageViewBookPhoto = (ImageView) itemView.findViewById(R.id.ivBookPhoto);
        mTextViewBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
        mTextViewBookAuthor = (TextView) itemView.findViewById(R.id.tvBookAuthor);
    }

}
