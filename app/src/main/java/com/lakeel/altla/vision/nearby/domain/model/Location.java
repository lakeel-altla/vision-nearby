package com.lakeel.altla.vision.nearby.domain.model;

import com.firebase.geofire.GeoLocation;

public final class Location {

    public final LocationMeta metaData;

    public final GeoLocation geoLocation;

    public Location(LocationMeta metaData, GeoLocation geoLocation) {
        this.metaData = metaData;
        this.geoLocation = geoLocation;
    }
}
