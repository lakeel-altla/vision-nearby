package com.lakeel.altla.vision.nearby.presentation.analytics;

enum AnalyticsProperty {
    BLE_STATE("ble_state");

    private final String value;

    AnalyticsProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
