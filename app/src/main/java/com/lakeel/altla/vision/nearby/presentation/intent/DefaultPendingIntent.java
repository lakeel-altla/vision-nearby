package com.lakeel.altla.vision.nearby.presentation.intent;

import android.content.Context;
import android.content.Intent;

public final class DefaultPendingIntent extends BasePendingIntent {

    public DefaultPendingIntent(Context context) {
        super(context);

        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
