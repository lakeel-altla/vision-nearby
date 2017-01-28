package com.lakeel.altla.vision.nearby.presentation.view.intent;

import android.content.Intent;
import android.net.Uri;

public final class GoogleMapIntent extends Intent {

    private static final String GOOGLE_MAP_DIRECTION_INTENT_QUERY = "http://maps.google.com/maps?daddr=";

    private static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";

    public GoogleMapIntent(double latitude, double longitude) {
        Uri uri = Uri.parse(GOOGLE_MAP_DIRECTION_INTENT_QUERY + String.valueOf(latitude) + "," + String.valueOf(longitude));
        setData(uri);
        setAction(Intent.ACTION_VIEW);
        setPackage(GOOGLE_MAP_PACKAGE);
    }
}
