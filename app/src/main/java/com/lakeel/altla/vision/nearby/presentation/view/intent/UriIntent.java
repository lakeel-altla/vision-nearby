package com.lakeel.altla.vision.nearby.presentation.view.intent;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public final class UriIntent extends Intent {

    public UriIntent(@NonNull String uri) {
        setAction(Intent.ACTION_VIEW);
        setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setData(Uri.parse(uri));
    }
}