package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class Beacon {

    @Exclude
    public String beaconId;

    public String userId;

    public String name;

    public String os;

    public String version;

    public boolean isLost;

    public Object lastUsedTime;
}