package com.lakeel.altla.vision.nearby.beacon;

public final class EddystoneUid {

    private final String beaconId;

    public EddystoneUid(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getNamespaceId() {
        return beaconId.substring(0, 20);
    }

    public String getInstanceId() {
        return beaconId.substring(20, 32);
    }
}
