package com.example.skpapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        if(remoteMessage.getNotification()!=null){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private RemoteViews getCustomDesign(String title, String message){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.notification_title,title);
        remoteViews.setTextViewText(R.id.notification_message,message);
        return remoteViews;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showNotification(String title, String message){
            Intent intent;
            intent = new Intent(this, HomeActivity.class);
            String channel_id="web_app_channel";
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                    .setSmallIcon(R.drawable.ic_skp)
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000,1000,1000,1000,1000})
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                builder=builder.setContent(getCustomDesign(title, message));
            }
            else {
                builder = builder.setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.ic_skp);
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(0, builder.build());


    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = userPref.edit();
        editor.putString("fcm_token", token);
        editor.apply();
    }
}