package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;
import android.support.annotation.NonNull;

public interface DistanceEstimationView {

    void showTitle(@NonNull String targetName);

    void startAnimation();

    void showDistanceMessage(@NonNull String distanceMessage);

    void requestAccessFineLocationPermission();

    void showBleEnabledActivity(@NonNull Intent intent);
}
