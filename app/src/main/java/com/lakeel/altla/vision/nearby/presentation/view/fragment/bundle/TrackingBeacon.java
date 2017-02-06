package com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle;

import java.io.Serializable;

public final class TrackingBeacon implements Serializable {

    public final String beaconId;

    public final String name;

    public TrackingBeacon(String beaconId, String name) {
        this.beaconId = beaconId;
        this.name = name;
    }
}
