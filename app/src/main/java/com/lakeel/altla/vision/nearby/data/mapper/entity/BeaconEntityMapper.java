package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;

public final class BeaconEntityMapper {

    public BeaconEntity map() {
        BeaconEntity entity = new BeaconEntity();
        entity.lastUsedTime = ServerValue.TIMESTAMP;
        return entity;
    }

    public BeaconEntity map(String userId, String deviceName) {
        BeaconEntity entity = new BeaconEntity();
        entity.userId = userId;
        entity.deviceName = deviceName;
        entity.lastUsedTime = ServerValue.TIMESTAMP;
        return entity;
    }
}
