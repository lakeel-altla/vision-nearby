package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showEmptyView();

    void showFoundDate(long foundTime);

    void launchGoogleMapApp(GoogleMapIntent intent);

    void showOptionMenu();

    void showDistanceEstimationFragment(EstimationTarget target);

    void showSnackBar(@StringRes int resId);
}
