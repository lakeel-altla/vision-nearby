package com.lakeel.altla.library;

/**
 * An Eddystone UID, broadcast by BLE beacons.
 */
public final class EddystoneUID {

    /**
     * The 16-byte ID, as a hex string.
     */
    private final String mBeaconId;

    /**
     * Constructs an EddystoneUID with the 16-byte ID, as a hex string.
     *
     * @param beaconId The 16-byte ID, as a hex string.
     * @throws IllegalArgumentNullException Thrown when beaconId is null.
     */
    public EddystoneUID(String beaconId) {
        if (beaconId == null) throw new IllegalArgumentNullException("beaconId");
        mBeaconId = beaconId;
    }

    /**
     * @return The namespace ID (first 10 bytes), as a hex string.
     */
    public String getNamespaceId() {
        return mBeaconId.substring(0, 20);
    }

    /**
     * @return The instance ID (last 6 bytes), as a hex string.
     */
    public String getInstanceId() {
        return mBeaconId.substring(20, 32);
    }
}
