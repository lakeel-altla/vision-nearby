package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class InformationEntity {
    
    public String title;

    public String body;

    public long postTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);
        map.put("postTime", ServerValue.TIMESTAMP);
        return map;
    }
}
