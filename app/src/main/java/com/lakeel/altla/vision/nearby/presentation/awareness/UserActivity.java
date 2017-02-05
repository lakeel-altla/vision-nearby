package com.lakeel.altla.vision.nearby.presentation.awareness;

import com.lakeel.altla.vision.nearby.R;

public enum UserActivity {
    IN_VEHICLE(com.google.android.gms.location.DetectedActivity.IN_VEHICLE, R.string.user_activity_vehicle),
    ON_BICYCLE(com.google.android.gms.location.DetectedActivity.ON_BICYCLE, R.string.user_activity_bicycle),
    ON_FOOT(com.google.android.gms.location.DetectedActivity.ON_FOOT, R.string.user_activity_foot),
    RUNNING(com.google.android.gms.location.DetectedActivity.RUNNING, R.string.user_activity_running),
    STILL(com.google.android.gms.location.DetectedActivity.STILL, R.string.user_activity_still),
    TILTING(com.google.android.gms.location.DetectedActivity.TILTING, R.string.user_activity_tilting),
    UNKNOWN(com.google.android.gms.location.DetectedActivity.UNKNOWN, R.string.user_activity_unknown),
    WALKING(com.google.android.gms.location.DetectedActivity.WALKING, R.string.user_activity_walking);

    private int intValue;

    private int resValue;

    UserActivity(int intValue, int resValue) {
        this.intValue = intValue;
        this.resValue = resValue;
    }

    public int getResValue() {
        return resValue;
    }

    public static UserActivity toUserActivity(Integer integerValue) {
        if (integerValue == null) {
            return UNKNOWN;
        }
        for (UserActivity currentActivity : values()) {
            if (currentActivity.intValue == integerValue) {
                return currentActivity;
            }
        }
        return UNKNOWN;
    }
}
