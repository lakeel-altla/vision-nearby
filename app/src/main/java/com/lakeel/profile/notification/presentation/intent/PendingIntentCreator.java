package com.lakeel.profile.notification.presentation.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class PendingIntentCreator {

    private Context mContext;

    private Intent mIntent;

    public PendingIntentCreator(Context context, Uri data) {
        mContext = context;

        mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.setData(data);
    }

    public PendingIntent create() {
        return PendingIntent.getActivity(mContext, 0, mIntent, 0);
    }
}
