package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;

public final class LocationMetaDataMapper {

    public LocationMetaData map(LocationDataEntity entity, String key) {
        LocationMetaData metaData = new LocationMetaData();
        metaData.locationMetaDataId = key;
        metaData.beaconId = entity.beaconId;
        metaData.passingTime = entity.passingTime;
        return metaData;
    }
}
