package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface DistanceEstimationView {

    void startAnimation();

    void showDistance(String meters);

    void requestAccessFineLocationPermission();

    void showBleEnabledActivity(Intent intent);
}
