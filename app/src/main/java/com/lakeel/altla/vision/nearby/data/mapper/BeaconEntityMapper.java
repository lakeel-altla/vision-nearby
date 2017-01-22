package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.BeaconEntity;

public final class BeaconEntityMapper {

    public BeaconEntity map(String userId, String name) {
        BeaconEntity entity = new BeaconEntity();
        entity.userId = userId;
        entity.name = name;
        return entity;
    }
}
