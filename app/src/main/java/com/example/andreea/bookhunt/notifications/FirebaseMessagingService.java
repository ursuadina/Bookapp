package com.example.andreea.bookhunt.notifications;

import android.app.Notification;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private Intent intent;
    public static final String TAG = "FIrebaseMess";
    public FirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        FirebaseMessaging.getInstance().subscribeToTopic("-LhAlUgxE0fJAKBCLl-u");
        Log.e(TAG, "onNewToken: newToken - " + s);

        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String s) {
        Log.e(TAG, "sendRegistrationToServer: TOKEN - " + s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //if(remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
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
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
