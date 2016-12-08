package com.lakeel.altla.vision.nearby.presentation.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.lakeel.altla.vision.nearby.R;

import java.util.UUID;

public final class LocalNotification {

    private Context context;

    private Notification notification;

    public LocalNotification(Context context, String title, String message, PendingIntent intent) {
        this.context = context;

        notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_nearby)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();
    }

    public void show() {
        NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(UUID.randomUUID().hashCode(), notification);
    }
}
