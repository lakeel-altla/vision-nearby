package com.lakeel.altla.vision.nearby.presentation.constants;

public enum AnalyticsParam {
    USER_ID("user_id"), USER_NAME("user_name");

    private String value;

    AnalyticsParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
