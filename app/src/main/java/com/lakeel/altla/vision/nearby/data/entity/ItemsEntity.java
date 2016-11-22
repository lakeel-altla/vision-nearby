package com.lakeel.altla.vision.nearby.data.entity;

import java.util.HashMap;

public final class ItemsEntity extends BaseEntity {

    public String name;

    public String imageUri;

    public String email;

    public HashMap<String, UserBeaconEntity> beacons;

    public static final class UserBeaconEntity {

        public long lastUsedTime;
    }
}
