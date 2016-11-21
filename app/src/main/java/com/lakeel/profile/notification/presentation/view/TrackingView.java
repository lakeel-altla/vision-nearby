package com.lakeel.profile.notification.presentation.view;

import com.firebase.geofire.GeoLocation;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showEmptyView();

    void showDetectedDate(String detectedTime);

    void showFindNearbyDeviceConfirmDialog();

    void showFindNearbyDeviceFragment(String beaconId, String beaconName);
}
