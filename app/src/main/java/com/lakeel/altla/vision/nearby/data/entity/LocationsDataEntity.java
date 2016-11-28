package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class LocationsDataEntity extends BaseEntity {

    public String id;

    public long passingTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", id);
        map.put("passingTime", ServerValue.TIMESTAMP);
        return map;
    }
}
