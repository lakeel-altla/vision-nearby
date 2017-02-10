package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.entity.LocationMetaDataEntity;

public final class LocationDataEntityMapper {

    public LocationMetaDataEntity map(String id) {
        LocationMetaDataEntity entity = new LocationMetaDataEntity();
        entity.beaconId = id;
        entity.passingTime = ServerValue.TIMESTAMP;
        return entity;
    }
}
