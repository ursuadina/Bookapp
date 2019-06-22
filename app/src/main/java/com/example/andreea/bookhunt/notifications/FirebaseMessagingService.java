package com.example.andreea.bookhunt.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.andreea.bookhunt.IndexActivity;
import com.example.andreea.bookhunt.R;
import com.example.andreea.bookhunt.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private Intent intent;
    public static final String TAG = "FIrebaseMess";
    public FirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String s) {


        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String s) {
        Log.e(TAG, "sendRegistrationToServer: TOKEN - " + s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //if(remoteMessage.getNotification() != null) {
       /* if(!remoteMessage.getData().get("userId").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Date date = new Date();
            Notification notification = new Notification(remoteMessage.getData().get("username"), remoteMessage.getNotification().getBody(), date.toString());
            FirebaseDatabase.getInstance().getReference("Notifications/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(notification);
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }*/
        Map<String, String> data = remoteMessage.getData();
        String userId = data.get("userId");
        String title = data.get("title");
        String body = data.get("body");
        String username = data.get("username");
        if(!userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Date date = new Date();
            Notification notification = new Notification(username, body, date.toString());
            FirebaseDatabase.getInstance().getReference("Notifications/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(notification);
            sendNotification(title, body);
        }
        //}
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title, String messageBody) {
//        if(click_action.equals("IndexActivity")) {
//            intent = new Intent(FirebaseMessagingService.this, IndexActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }

        intent = new Intent(this, IndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Default", "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
