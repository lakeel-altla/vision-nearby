package com.lakeel.altla.vision.nearby.presentation.view;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showEmptyView();

    void showDetectedDate(long detectedTime);

    void launchGoogleMapApp(String latitude, String longitude);

    void showOptionMenu();

    void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String beaconName);
}
