package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class LocationMetaDataEntity {

    private static final String BEACON_ID = "beaconId";

    private static final String PASSING_TIME = "passingTime";

    public String beaconId;

    public long passingTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(BEACON_ID, beaconId);
        map.put(PASSING_TIME, ServerValue.TIMESTAMP);
        return map;
    }
}