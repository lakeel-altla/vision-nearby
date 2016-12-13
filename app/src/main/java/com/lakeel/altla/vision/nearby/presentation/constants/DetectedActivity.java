package com.lakeel.altla.vision.nearby.presentation.constants;

import com.lakeel.altla.vision.nearby.R;

public enum DetectedActivity {
    IN_VEHICLE(com.google.android.gms.location.DetectedActivity.IN_VEHICLE, R.string.detected_activity_vehicle),
    ON_BICYCLE(com.google.android.gms.location.DetectedActivity.ON_BICYCLE, R.string.detected_activity_bicycle),
    ON_FOOT(com.google.android.gms.location.DetectedActivity.ON_FOOT, R.string.detected_activity_foot),
    RUNNING(com.google.android.gms.location.DetectedActivity.RUNNING, R.string.detected_activity_running),
    STILL(com.google.android.gms.location.DetectedActivity.STILL, R.string.detected_activity_still),
    TILTING(com.google.android.gms.location.DetectedActivity.TILTING, R.string.detected_activity_tilting),
    UNKNOWN(com.google.android.gms.location.DetectedActivity.UNKNOWN, R.string.detected_activity_unknown),
    WALKING(com.google.android.gms.location.DetectedActivity.WALKING, R.string.detected_activity_walking);

    private int intValue;

    private int resValue;

    DetectedActivity(int intValue, int resValue) {
        this.intValue = intValue;
        this.resValue = resValue;
    }

    public int getResValue() {
        return resValue;
    }

    public static DetectedActivity toDetectedActivity(int intValue) {
        for (DetectedActivity currentActivity : DetectedActivity.values()) {
            if (currentActivity.intValue == intValue) {
                return currentActivity;
            }
        }
        return DetectedActivity.UNKNOWN;
    }
}
