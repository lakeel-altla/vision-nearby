package com.lakeel.altla.vision.nearby.data.entity;

import com.firebase.geofire.GeoLocation;

public final class LocationEntity {

    public final LocationMetaDataEntity locationDataEntity;

    public final GeoLocation geoLocation;

    public LocationEntity(LocationMetaDataEntity locationDataEntity, GeoLocation geoLocation) {
        this.locationDataEntity = locationDataEntity;
        this.geoLocation = geoLocation;
    }
}
