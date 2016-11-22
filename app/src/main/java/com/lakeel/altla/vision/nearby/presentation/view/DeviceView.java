package com.lakeel.altla.vision.nearby.presentation.view;

public interface DeviceView {

    void updateItems();

    void showTrackingFragment(String beaconId, String beaconName);
}
