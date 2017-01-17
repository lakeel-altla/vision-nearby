package com.lakeel.altla.vision.nearby.presentation.constants;

public enum AnalyticsEvent {
    LOG_IN("log_in"),
    LOG_OUT("log_out"),
    ADD_FAVORITE("add_favorite"),
    REMOVE_FAVORITE("remove_favorite"),
    ON_ADVERTISE("on_advertise"),
    OFF_ADVERTISE("off_advertise"),
    ON_SUBSCRIBE("on_subscribe"),
    OFF_SUBSCRIBE("off_subscribe"),
    SAVE_LINE_URL("save_line_url");

    private String value;

    AnalyticsEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
