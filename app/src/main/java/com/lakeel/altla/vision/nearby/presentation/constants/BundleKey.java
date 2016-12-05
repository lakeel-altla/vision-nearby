package com.lakeel.altla.vision.nearby.presentation.constants;

public enum BundleKey {

    BEACON_IDS("beaconIds"),
    BEACON_ID("beaconId"),
    USER_ID("userId"),
    USER_NAME("userName"),
    TARGET_NAME("targetName"),
    RECENTLY("recently");

    private String value;

    BundleKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
