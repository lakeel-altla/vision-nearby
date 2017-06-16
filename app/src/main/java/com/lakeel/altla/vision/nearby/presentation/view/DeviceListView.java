package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface DeviceListView {

    void updateItems();

    void removeAll(@IntRange(from = 0) int size);

    void showTrackingFragment(@NonNull String beaconId, @NonNull String name);

    void showSnackBar(@StringRes int resId);
}
