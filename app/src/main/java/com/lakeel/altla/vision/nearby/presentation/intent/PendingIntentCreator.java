package com.lakeel.altla.vision.nearby.presentation.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class PendingIntentCreator {

    private Context context;

    private Intent intent;

    public PendingIntentCreator(Context context, Uri data) {
        this.context = context;

        intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setData(data);
    }

    public PendingIntent create() {
        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
