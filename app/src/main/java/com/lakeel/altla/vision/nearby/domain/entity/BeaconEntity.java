package com.lakeel.altla.vision.nearby.domain.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class BeaconEntity {

    public String beaconId;

    public String name;

    public String userId;

    public boolean isLost;

    public long lastUsedTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", name);
        map.put("userId", userId);
        map.put("lastUsedTime", ServerValue.TIMESTAMP);
        return map;
    }

}