package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class BeaconEntity {

    public String name;

    public String userId;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", name);
        map.put("userId", userId);
        map.put("lastUsedTime", ServerValue.TIMESTAMP);
        return map;
    }

}
