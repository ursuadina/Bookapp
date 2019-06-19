package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private Context context;
    private ArrayList<User> users;

    private String username;
    private String firstName;
    private String lastName;
    private String lastDate;
    private User user;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        user = users.get(i);
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        lastDate = user.getLastDate();

        userViewHolder.tvlastLoggedIn.setText(lastDate);
        userViewHolder.tvUsername.setText(username);
        userViewHolder.tvFNAdmin.setText(firstName);
        userViewHolder.tvLNAdmin.setText(lastName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}