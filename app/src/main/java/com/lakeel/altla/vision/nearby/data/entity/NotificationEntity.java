package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class NotificationEntity {

    public String to;

    public String title;

    public String message;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("to", to);
        map.put("title", title);
        map.put("message", message);
        map.put("sendTime", ServerValue.TIMESTAMP);

        return map;
    }
}
