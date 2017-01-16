package com.lakeel.altla.library;

/**
 * An Eddystone UID, broadcast by BLE beacons.
 */
public final class EddystoneUid {

    /**
     * The 16-byte ID, as a hex string.
     */
    private final String beaconId;

    /**
     * Constructs an EddystoneUID with the 16-byte ID, as a hex string.
     *
     * @param beaconId The 16-byte ID, as a hex string.
     * @throws IllegalArgumentNullException Thrown when beaconId is null.
     */
    public EddystoneUid(String beaconId) {
        if (beaconId == null) throw new IllegalArgumentNullException("beaconId");
        this.beaconId = beaconId;
    }

    /**
     * @return The namespace ID (first 10 bytes), as a hex string.
     */
    public String getNamespaceId() {
        return beaconId.substring(0, 20);
    }

    /**
     * @return The instance ID (last 6 bytes), as a hex string.
     */
    public String getInstanceId() {
        return beaconId.substring(20, 32);
    }
}
