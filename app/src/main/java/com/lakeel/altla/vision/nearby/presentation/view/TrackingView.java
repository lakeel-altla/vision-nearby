package com.lakeel.altla.vision.nearby.presentation.view;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;

import java.util.ArrayList;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showEmptyView();

    void showFoundDate(long foundTime);

    void launchGoogleMapApp(GoogleMapIntent intent);

    void showOptionMenu();

    void showDistanceEstimationFragment(ArrayList<String> beaconIds, String beaconName);
}
