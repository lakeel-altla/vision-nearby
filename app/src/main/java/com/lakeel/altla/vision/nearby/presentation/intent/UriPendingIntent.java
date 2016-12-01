package com.lakeel.altla.vision.nearby.presentation.intent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class UriPendingIntent extends BasePendingIntent {

    public UriPendingIntent(Context context, Uri data) {
        super(context);

        Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setData(data);
    }
}
