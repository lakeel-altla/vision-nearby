package com.lakeel.altla.vision.nearby.presentation.view.notification;

import com.lakeel.altla.vision.nearby.R;

import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.UUID;

public final class Notification {

    private android.app.Notification notification;

    private android.app.NotificationManager notificationManager;

    public static class Builder {

        private Context context;

        private String title;

        private String text;

        private PendingIntent intent;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(@StringRes int resId) {
            title = context.getResources().getString(resId);
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder text(@StringRes int resId, Object... formatArgs) {
            text = context.getResources().getString(resId, formatArgs);
            return this;
        }

        public Builder intent(PendingIntent intent) {
            this.intent = intent;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    public Notification(Builder builder) {
        notification = new NotificationCompat.Builder(builder.context)
                .setContentTitle(builder.title)
                .setTicker(builder.text)
                .setContentText(builder.text)
                .setSmallIcon(R.mipmap.ic_nearby)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setAutoCancel(true)
                .setContentIntent(builder.intent)
                .setColor(ContextCompat.getColor(builder.context, R.color.colorPrimary))
                .build();
        notificationManager = (android.app.NotificationManager) builder.context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyNotification() {
        notificationManager.notify(UUID.randomUUID().hashCode(), notification);
    }
}
