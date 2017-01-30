package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class InformationEntity {

    private static final String TITLE = "title";

    private static final String BODY = "body";

    private static final String POST_TIME = "postTime";

    public String title;

    public String body;

    public long postTime;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TITLE, title);
        map.put(BODY, body);
        map.put(POST_TIME, ServerValue.TIMESTAMP);
        return map;
    }
}
