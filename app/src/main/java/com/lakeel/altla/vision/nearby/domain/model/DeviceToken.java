package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class DeviceToken {

    @Exclude
    public String userId;

    @Exclude
    public String beaconId;

    public String token;
}
