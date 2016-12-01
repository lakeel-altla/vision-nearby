package com.lakeel.altla.vision.nearby.presentation.intent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class BasePendingIntent {

    protected final Context context;

    protected final Intent intent;

    public BasePendingIntent(Context context) {
        this.context = context;
        this.intent = new Intent(Intent.ACTION_VIEW);
    }

    protected Intent getIntent() {
        return intent;
    }

    public PendingIntent create() {
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
