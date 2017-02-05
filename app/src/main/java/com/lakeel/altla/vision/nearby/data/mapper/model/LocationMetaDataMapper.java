package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.LocationMetaDataEntity;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;

public final class LocationMetaDataMapper {

    public LocationMetaData map(DataSnapshot snapshot) {
        LocationMetaDataEntity entity = snapshot.getValue(LocationMetaDataEntity.class);

        LocationMetaData metaData = new LocationMetaData();
        metaData.locationMetaDataId = snapshot.getKey();
        metaData.beaconId = entity.beaconId;
        metaData.passingTime = entity.passingTime;

        return metaData;
    }
}
