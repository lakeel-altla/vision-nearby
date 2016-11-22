package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.LocationsDataEntity;

public final class LocationsDataEntityMapper {

    public LocationsDataEntity map(String id) {
        LocationsDataEntity entity = new LocationsDataEntity();
        entity.id = id;
        return entity;
    }
}
