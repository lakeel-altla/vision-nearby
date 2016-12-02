package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class FavoriteEntity extends BaseEntity {

    public long addedTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("addedTime", ServerValue.TIMESTAMP);
        return map;
    }
}
