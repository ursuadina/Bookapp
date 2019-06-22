package com.example.andreea.bookhunt.recyclerviewutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        Notification notification = notifications.get(i);
        String from = "From: " + notification.getFrom();
        String message = "Message: " + notification.getMessage();
        String receivedAt = notification.getReceivedAt();

        notificationViewHolder.mTextViewReceivedTime.setText(receivedAt);
        notificationViewHolder.mTextViewMessage.setText(message);
        notificationViewHolder.mTextViewFrom.setText(from);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void deleteItem(int i){
        Notification notification = notifications.get(i);
        FirebaseDatabase.getInstance().getReference("Notifications/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+notification.getId()).removeValue();
    }
}
