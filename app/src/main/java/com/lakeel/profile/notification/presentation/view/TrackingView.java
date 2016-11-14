package com.lakeel.profile.notification.presentation.view;

import com.firebase.geofire.GeoLocation;

public interface TrackingView {

    void showLocationMap(GeoLocation location);
}
