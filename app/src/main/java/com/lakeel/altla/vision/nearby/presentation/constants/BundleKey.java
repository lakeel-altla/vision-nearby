package com.lakeel.altla.vision.nearby.presentation.constants;

public enum BundleKey {

    BEACON_IDS("beaconIds"), BEACON_ID("beaconId"), USER_ID("userId"), USER_NAME("userName"),TARGET_NAME("targetName");

    private String mValue;

    BundleKey(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }
}
