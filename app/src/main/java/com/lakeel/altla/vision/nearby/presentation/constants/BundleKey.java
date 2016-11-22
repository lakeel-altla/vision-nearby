package com.lakeel.altla.vision.nearby.presentation.constants;

public enum BundleKey {

    BEACON_ID("beaconId"), BEACON_NAME("beaconName");

    private String mValue;

    BundleKey(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
