package com.lakeel.altla.vision.nearby.presentation.analytics;

public enum AnalyticsEvent {
    LOG_OUT("logout"),
    VIEW_FAVORITE_ITEM("view_favorite_item"),
    ADD_FAVORITE("add_favorite"),
    REMOVE_FAVORITE("remove_favorite"),
    ON_ADVERTISE("on_advertise"),
    OFF_ADVERTISE("off_advertise"),
    ON_SUBSCRIBE("on_subscribe"),
    OFF_SUBSCRIBE("off_subscribe"),
    INPUT_LINE_URL("input_line_url"),
    FOUND_DEVICE("found_device"),
    LOST_DEVICE("lost_device"),
    REMOVE_DEVICE("remove_device"),
    LAUNCH_GOOGLE_MAP("launch_google_map"),
    ESTIMATE_DISTANCE("estimate_distance"),
    VIEW_HISTORY_ITEM("view_history_item"),
    ADD_HISTORY("add_history"),
    REMOVE_HISTORY("remove_history");

    private String value;

    AnalyticsEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
