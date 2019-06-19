package com.example.andreea.bookhunt.recyclerviewutils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.andreea.bookhunt.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public TextView tvLNAdmin;
    public TextView tvFNAdmin;
    public TextView tvUsername;
    public TextView tvlastLoggedIn;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }

    private void initView(View itemView) {
        tvLNAdmin = (TextView) itemView.findViewById(R.id.textViewLNAdmin);
        tvFNAdmin = (TextView) itemView.findViewById(R.id.textViewFNAdmin);
        tvUsername = (TextView) itemView.findViewById(R.id.textViewUsernameAdmin);
        tvlastLoggedIn = (TextView) itemView.findViewById(R.id.textViewLastLoggedIn);

    }
}
