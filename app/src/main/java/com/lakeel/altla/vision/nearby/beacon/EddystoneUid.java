package com.lakeel.altla.vision.nearby.beacon;

import java.util.UUID;

public final class EddystoneUid {

    private final String beaconId;

    public EddystoneUid() {
        String uuid = UUID.randomUUID().toString();
        String replacedString = uuid.replace("-", "");
        // Remove 5 - 10 bytes.
        String namespaceId = replacedString.substring(0, 8) + replacedString.substring(20, 32);
        String instanceId = "000000000001";
        beaconId = namespaceId + instanceId;
    }

    public EddystoneUid(String beaconId) {
        // TODO: Check the format.
        this.beaconId = beaconId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public String getNamespaceId() {
        return beaconId.substring(0, 20);
    }

    public String getInstanceId() {
        return beaconId.substring(20, 32);
    }
}
