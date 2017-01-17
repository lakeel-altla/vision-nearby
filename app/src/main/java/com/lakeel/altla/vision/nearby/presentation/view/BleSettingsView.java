package com.lakeel.altla.vision.nearby.presentation.view;

public interface BleSettingsView {

    void startAdvertise(String beaconId);

    void disableAdvertiseSettings();

    void startSubscribe();

    void stopSubscribe();
}
