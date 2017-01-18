package com.lakeel.altla.vision.nearby.presentation.constants;

public enum AnalyticsProperty {
    BLE_STATE("ble_state");

    private String value;

    AnalyticsProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
