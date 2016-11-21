package com.lakeel.altla.library;

public final class EddystoneUID {

    private final String mBeaconId;

    public EddystoneUID(String beaconId) {
        if (beaconId == null) throw new IllegalNullException("beaconId");
        mBeaconId = beaconId;
    }

    public String getNamespaceId() {
        return mBeaconId.substring(0, 20);
    }

    public String getInstanceId() {
        return mBeaconId.substring(20, 32);
    }
}
