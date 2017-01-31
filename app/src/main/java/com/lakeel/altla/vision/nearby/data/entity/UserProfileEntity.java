package com.lakeel.altla.vision.nearby.data.entity;

import java.util.HashMap;
import java.util.Map;

public final class UserProfileEntity {

    private static final String NAME = "name";

    private static final String IMAGE_URI = "imageUri";

    private static final String EMAIL = "email";

    public String name;

    public String imageUri;

    public String email;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, name);
        map.put(IMAGE_URI, imageUri);
        map.put(EMAIL, email);
        return map;
    }
}
