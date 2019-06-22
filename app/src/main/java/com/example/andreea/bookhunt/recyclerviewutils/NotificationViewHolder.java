package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewFrom;
    public TextView mTextViewMessage;
    public TextView mTextViewReceivedTime;
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        mTextViewFrom = (TextView) itemView.findViewById(R.id.textViewFromNotif);
        mTextViewMessage = (TextView) itemView.findViewById(R.id.textViewMessageNotif);
        mTextViewReceivedTime = (TextView) itemView.findViewById(R.id.textViewReceivedAt);
    }
}
