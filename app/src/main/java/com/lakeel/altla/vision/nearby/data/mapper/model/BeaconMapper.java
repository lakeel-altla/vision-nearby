package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

public final class BeaconMapper {

    public Beacon map(DataSnapshot snapshot) {
        BeaconEntity entity = snapshot.getValue(BeaconEntity.class);

        Beacon beacon = new Beacon();
        beacon.beaconId = snapshot.getKey();
        beacon.name = entity.deviceName;
        beacon.userId = entity.userId;
        beacon.isLost = entity.isLost;
        beacon.lastUsedTime = entity.lastUsedTime;
        return beacon;
    }
}
