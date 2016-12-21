package com.lakeel.altla.vision.nearby.presentation.constants;

public enum AnalyticsEvent {
    ADD_FAVORITE("add_favorite");

    private String value;

    AnalyticsEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
