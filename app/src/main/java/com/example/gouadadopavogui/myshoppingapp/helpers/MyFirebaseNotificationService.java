package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.ui.MyShoppingCartUI;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by GouadaDopavogui on 05.02.2017.
 */

public class MyFirebaseNotificationService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder notificationcompatBuilder = new NotificationCompat.Builder(this);

        Intent notificationintent = new Intent(this, MyShoppingCartUI.class);
        notificationintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationintent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //notificationcompatBuilder.setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
        notificationcompatBuilder.setSmallIcon(R.drawable.cart_s)
                .setAutoCancel(true)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent)
                .setSound(soundUri);
        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationcompatBuilder.build());
    }
}


