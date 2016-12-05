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

        private Context mContext;

        private String mTitle;

        private String mText;

        private PendingIntent mIntent;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder title(@StringRes int resId) {
            mTitle = mContext.getResources().getString(resId);
            return this;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder text(String text) {
            mText = text;
            return this;
        }

        public Builder text(@StringRes int resId, Object... formatArgs) {
            mText = mContext.getResources().getString(resId, formatArgs);
            return this;
        }

        public Builder intent(PendingIntent intent) {
            mIntent = intent;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    public Notification(Builder builder) {
        notification = new NotificationCompat.Builder(builder.mContext)
                .setContentTitle(builder.mTitle)
                .setTicker(builder.mText)
                .setContentText(builder.mText)
                .setSmallIcon(R.mipmap.ic_nearby)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setAutoCancel(true)
                .setContentIntent(builder.mIntent)
                .setColor(ContextCompat.getColor(builder.mContext, R.color.colorPrimary))
                .build();
        notificationManager = (android.app.NotificationManager) builder.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyNotification() {
        notificationManager.notify(UUID.randomUUID().hashCode(), notification);
    }
}
