package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface DistanceEstimationView {

    void showDistance(String meters);

    void showBleEnabledActivity(Intent intent);
}
