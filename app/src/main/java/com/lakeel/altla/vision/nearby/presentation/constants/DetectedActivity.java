package com.lakeel.altla.vision.nearby.presentation.constants;

public enum DetectedActivity {
    IN_VEHICLE(com.google.android.gms.location.DetectedActivity.IN_VEHICLE, "Vehicle"),
    ON_BICYCLE(com.google.android.gms.location.DetectedActivity.ON_BICYCLE, "Bicycle"),
    ON_FOOT(com.google.android.gms.location.DetectedActivity.ON_FOOT, "Foot"),
    RUNNING(com.google.android.gms.location.DetectedActivity.RUNNING, "Running"),
    STILL(com.google.android.gms.location.DetectedActivity.STILL, "Still"),
    TILTING(com.google.android.gms.location.DetectedActivity.TILTING, "Tilting"),
    UNKNOWN(com.google.android.gms.location.DetectedActivity.UNKNOWN, "Unknown"),
    WALKING(com.google.android.gms.location.DetectedActivity.WALKING, "Walking");

    private int intValue;

    private String stringValue;

    DetectedActivity(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public String getValue() {
        return stringValue;
    }

    public static DetectedActivity toUserActivity(int intValue) {
        for (DetectedActivity currentActivity : DetectedActivity.values()) {
            if (currentActivity.intValue == intValue) {
                return currentActivity;
            }
        }
        return DetectedActivity.UNKNOWN;
    }
}
