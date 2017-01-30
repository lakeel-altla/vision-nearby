package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class BeaconEntity {

    private static final String USER_ID = "userId";

    private static final String USER_NAME = "userName";

    private static final String LAST_USED_TIME = "lastUsedTime";

    public String userId;

    public String userName;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(USER_ID, userId);
        map.put(USER_NAME, userName);
        map.put(LAST_USED_TIME, ServerValue.TIMESTAMP);
        return map;
    }

}
