package com.lakeel.altla.vision.nearby.presentation.view;

import com.firebase.geofire.GeoLocation;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showEmptyView();

    void showDetectedDate(String detectedTime);

    void launchGoogleMapApp(String latitude, String longitude);

    void showOptionMenu();

    void showFindNearbyDeviceFragment(String beaconId, String beaconName);
}
