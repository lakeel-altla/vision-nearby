package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

public interface BleSettingsView {

    void disableAdvertiseSettings();

    void startAdvertiseInBackground(@NonNull String beaconId);

    void startSubscribeInBackground();

    void stopSubscribeInBackground();

    void showSnackBar(@StringRes int resId);
}
