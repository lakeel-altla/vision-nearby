package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;

public final class LocationDataEntityMapper {

    public LocationDataEntity map(String id) {
        LocationDataEntity entity = new LocationDataEntity();
        entity.beaconId = id;
        return entity;
    }
}
