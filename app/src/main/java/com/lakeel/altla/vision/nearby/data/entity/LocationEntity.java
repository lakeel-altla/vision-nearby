package com.lakeel.altla.vision.nearby.data.entity;

import com.firebase.geofire.GeoLocation;

public final class LocationEntity {

    public final LocationDataEntity locationDataEntity;

    public final GeoLocation geoLocation;

    public LocationEntity(LocationDataEntity locationDataEntity, GeoLocation geoLocation) {
        this.locationDataEntity = locationDataEntity;
        this.geoLocation = geoLocation;
    }
}
