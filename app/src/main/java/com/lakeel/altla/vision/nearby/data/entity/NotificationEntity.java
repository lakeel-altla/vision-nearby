package com.lakeel.altla.vision.nearby.data.entity;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public final class NotificationEntity {

    private static final String TO = "to";

    private static final String TITLE = "title";

    private static final String MESSAGE = "message";

    private static final String REGISTRATION_TIME = "registrationTime";

    public String to;

    public String title;

    public String message;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TO, to);
        map.put(TITLE, title);
        map.put(MESSAGE, message);
        map.put(REGISTRATION_TIME, ServerValue.TIMESTAMP);
        return map;
    }
}