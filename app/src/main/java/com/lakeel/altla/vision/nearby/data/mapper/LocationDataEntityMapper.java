package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.LocationDataEntity;

public final class LocationDataEntityMapper {

    public LocationDataEntity map(String id) {
        LocationDataEntity entity = new LocationDataEntity();
        entity.beaconId = id;
        return entity;
    }
}
