package com.lakeel.altla.vision.nearby.presentation.beacon.distance;

import java.util.Locale;

public final class Distance {

    private final int txPower;

    private final int rssi;

    public Distance(int txPower, int rssi) {
        this.txPower = txPower;
        this.rssi = rssi;
    }

    public String getDistance() {
        double value = Math.pow(10d, ((double) txPower - rssi) / 20.0) / 100;
        // Return distance in meters.
        return String.format(Locale.getDefault(), "%.2f", value);
    }
}
