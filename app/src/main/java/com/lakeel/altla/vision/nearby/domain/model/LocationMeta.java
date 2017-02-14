package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class LocationMeta {

    @Exclude
    public String userId;

    @Exclude
    public String locationMetaDataId;

    public String beaconId;

    public Object passingTime;
}