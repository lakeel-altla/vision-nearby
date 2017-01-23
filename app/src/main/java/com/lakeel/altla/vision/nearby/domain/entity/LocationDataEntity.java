package com.lakeel.altla.vision.nearby.domain.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class LocationDataEntity {

    public String uniqueId;

    public String beaconId;

    public long passingTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("beaconId", beaconId);
        map.put("passingTime", ServerValue.TIMESTAMP);
        return map;
    }
}
