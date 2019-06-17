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
        if(remoteMessage.getData().size() > 0) {
            Log.e("FirebaseMessag", "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMesssage = data.getString("extra_info"); // camp din json
                Log.e("FirebaseMessaging", "onMessageReceived: ");
                Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
                Intent intent = new Intent(this, IndexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                String channelId = "Default";
                NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
//                    manager.createNotificationChannel(channel);
//                }
                manager.notify(0, builder.build());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
            Intent intent = new Intent(this, IndexActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setContentTitle(remoteMessage.getData().get("title"))
//                    .setContentText(remoteMessage.getData().get("text"))
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .build();
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//            manager.notify(123, notification);
//            Intent intent = new Intent(this, IndexActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            String channelId = "Default";
//            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(remoteMessage.getNotification().getTitle())
//                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
////                    manager.createNotificationChannel(channel);
////                }
//            manager.notify(0, builder.build());
            Log.e("FirebaseMessaging", "onMessageReceived: Title - " + title);
            Log.e("FirebaseMessaging", "onMessageReceived: Message - " + message);
            Log.e("FirebaseMessaging", "onMessageReceived: Click_action - " + click_action);
            Toast.makeText(getApplicationContext(), "New notification: " + title + "\nMessage:  " + message, Toast.LENGTH_SHORT).show();
            //sendNotification(title, message, click_action);
        }
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendNotification(String title, String messageBody, String click_action) {
//        if(click_action.equals("IndexActivity")) {
//            intent = new Intent(FirebaseMessagingService.this, IndexActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        }
        intent = new Intent(this, IndexActivity.class);
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
