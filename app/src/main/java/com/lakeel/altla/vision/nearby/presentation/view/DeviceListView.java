package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.IntRange;
import android.support.annotation.StringRes;

public interface DeviceListView {

    void updateItems();

    void showTrackingFragment(String beaconId, String beaconName);

    void removeAll(@IntRange(from = 0) int size);

    void showSnackBar(@StringRes int resId);
}
