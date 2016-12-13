package com.lakeel.altla.vision.nearby.presentation.intent;

import android.content.Intent;
import android.net.Uri;

public final class GoogleMapDirectionIntent extends Intent {

    private static final String GOOGLE_MAP_DIRECTION_INTENT_QUERY = "http://maps.google.com/maps?daddr=";

    private static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";

    public GoogleMapDirectionIntent(String latitude, String longitude) {
        Uri uri = Uri.parse(GOOGLE_MAP_DIRECTION_INTENT_QUERY + latitude + "," + longitude);
        setData(uri);
        setAction(Intent.ACTION_VIEW);
        setPackage(GOOGLE_MAP_PACKAGE);
    }
}
