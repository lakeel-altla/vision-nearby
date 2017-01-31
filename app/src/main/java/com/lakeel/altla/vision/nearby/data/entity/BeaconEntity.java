package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class BeaconEntity {

    private static final String USER_ID = "userId";

    private static final String DEVICE_NAME = "deviceName";

    private static final String LAST_USED_TIME = "lastUsedTime";

    public String userId;

    public String deviceName;

    public long lastUsedTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, userId);
        map.put(DEVICE_NAME, deviceName);
        map.put(LAST_USED_TIME, ServerValue.TIMESTAMP);
        return map;
    }
}
