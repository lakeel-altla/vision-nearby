package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface DistanceEstimationView {

    void showTitle(String targetName);

    void startAnimation();

    void showDistanceMessage(String distanceMessage);

    void requestAccessFineLocationPermission();

    void showBleEnabledActivity(Intent intent);
}
