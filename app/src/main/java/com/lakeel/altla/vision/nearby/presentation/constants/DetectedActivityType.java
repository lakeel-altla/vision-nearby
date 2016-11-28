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

    private int mIntValue;

    private String mStringValue;

    DetectedActivityType(int intValue, String stringValue) {
        mIntValue = intValue;
        mStringValue = stringValue;
    }

    public String getActivity() {
        return mStringValue;
    }

    public static DetectedActivityType toUserActivity(int intValue) {
        for (DetectedActivityType currentActivity : DetectedActivityType.values()) {
            if (currentActivity.mIntValue == intValue) {
                return currentActivity;
            }
        }
        return DetectedActivityType.UNKNOWN;
    }
}
