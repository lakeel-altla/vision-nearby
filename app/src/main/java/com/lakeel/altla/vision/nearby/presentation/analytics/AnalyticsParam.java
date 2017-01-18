package com.lakeel.altla.vision.nearby.presentation.analytics;

public enum AnalyticsParam {
    USER_ID("user_id"),
    USER_NAME("user_name"),
    FAVORITE_USER_ID("favorite_user_id"),
    FAVORITE_USER_NAME("favorite_user_name"),
    DEVICE_ID("device_id"),
    DEVICE_NAME("device_name"),
    TARGET_NAME("target_name"),
    HISTORY_USER_ID("history_user_id"),
    HISTORY_USER_NAME("history_user_name");

    private String value;

    AnalyticsParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
