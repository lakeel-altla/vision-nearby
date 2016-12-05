package com.lakeel.altla.vision.nearby.presentation.constants;

import com.google.android.gms.location.DetectedActivity;

public enum DetectedActivityType {
    IN_VEHICLE(DetectedActivity.IN_VEHICLE, "Vehicle"),
    ON_BICYCLE(DetectedActivity.ON_BICYCLE, "Bicycle"),
    ON_FOOT(DetectedActivity.ON_FOOT, "Foot"),
    RUNNING(DetectedActivity.RUNNING, "Running"),
    STILL(DetectedActivity.STILL, "Still"),
    TILTING(DetectedActivity.TILTING, "Tilting"),
    UNKNOWN(DetectedActivity.UNKNOWN, "Unknown"),
    WALKING(DetectedActivity.WALKING, "Walking");

    private int intValue;

    private String stringValue;

    DetectedActivityType(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public String getValue() {
        return stringValue;
    }

    public static DetectedActivityType toUserActivity(int intValue) {
        for (DetectedActivityType currentActivity : DetectedActivityType.values()) {
            if (currentActivity.intValue == intValue) {
                return currentActivity;
            }
        }
        return DetectedActivityType.UNKNOWN;
    }
}
