package com.lakeel.altla.vision.nearby.presentation.view;

public interface BleSettingsView {

    void disableAdvertiseSettings();

    void startAdvertiseInBackground(String beaconId);

    void startSubscribeInBackground();

    void stopSubscribeInBackground();
}
