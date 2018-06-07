package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.intent.GoogleMapIntent;

public interface TrackingView {

    void showLocationMap(@NonNull GeoLocation location);

    void showEmptyView();

    void showFoundDate(@NonNull String dateText);

    void launchGoogleMapApp(@NonNull GoogleMapIntent intent);

    void showOptionMenu();

    void showDistanceEstimationFragment(@NonNull EstimationTarget target);

    void showSnackBar(@StringRes int resId);
}
