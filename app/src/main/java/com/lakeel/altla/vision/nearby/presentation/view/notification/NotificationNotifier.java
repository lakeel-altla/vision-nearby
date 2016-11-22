package com.lakeel.altla.vision.nearby.presentation.view.notification;

import com.lakeel.altla.vision.nearby.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.UUID;

public final class NotificationNotifier {

    private Notification mNotification;

    private android.app.NotificationManager mManager;

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

        public Builder text(@StringRes int resId, Object... formatArgs) {
            mText = mContext.getResources().getString(resId, formatArgs);
            return this;
        }

        public Builder intent(PendingIntent intent) {
            mIntent = intent;
            return this;
        }

        public NotificationNotifier build() {
            return new NotificationNotifier(this);
        }
    }

    public NotificationNotifier(Builder builder) {
        mNotification = new NotificationCompat.Builder(builder.mContext)
                .setContentTitle(builder.mTitle)
                .setTicker(builder.mText)
                .setContentText(builder.mText)
                .setSmallIcon(R.mipmap.ic_nearby_white)
                .setVibrate(new long[]{0, 200, 100, 200, 100, 200})
                .setAutoCancel(true)
                .setContentIntent(builder.mIntent)
                .setColor(ContextCompat.getColor(builder.mContext, R.color.colorPrimary))
                .build();
        mManager = (android.app.NotificationManager) builder.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyNotification() {
        mManager.notify(UUID.randomUUID().hashCode(), mNotification);
    }
}
