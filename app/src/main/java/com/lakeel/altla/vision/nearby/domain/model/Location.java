package com.lakeel.altla.vision.nearby.domain.model;

import com.firebase.geofire.GeoLocation;

public final class Location {

    public final LocationMetaData metaData;

    public final GeoLocation geoLocation;

    public Location(LocationMetaData metaData, GeoLocation geoLocation) {
        this.metaData = metaData;
        this.geoLocation = geoLocation;
    }
}
