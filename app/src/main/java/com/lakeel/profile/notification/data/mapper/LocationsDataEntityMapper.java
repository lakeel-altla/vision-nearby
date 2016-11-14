package com.lakeel.profile.notification.data.mapper;

import com.lakeel.profile.notification.data.entity.LocationsDataEntity;

public final class LocationsDataEntityMapper {

    public LocationsDataEntity map(String id) {
        LocationsDataEntity entity = new LocationsDataEntity();
        entity.id = id;
        return entity;
    }
}
