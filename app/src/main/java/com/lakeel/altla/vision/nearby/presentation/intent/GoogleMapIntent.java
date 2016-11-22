package com.lakeel.altla.vision.nearby.presentation.intent;

import android.content.Intent;
import android.net.Uri;

public final class GoogleMapIntent {

    private static final String GOOGLE_MAP_DIRECTION_INTENT_QUERY = "http://maps.google.com/maps?daddr=";

    private static final String GOOGLE_MAP_PACKAGE = "com.google.android.apps.maps";

    private final String mLatitude;

    private final String mLongitude;

    public GoogleMapIntent(String latitude, String longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Intent create() {
        String uriText = GOOGLE_MAP_DIRECTION_INTENT_QUERY + mLatitude + "," + mLongitude;
        Uri uri = Uri.parse(uriText);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage(GOOGLE_MAP_PACKAGE);
        return mapIntent;
    }
}
