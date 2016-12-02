package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class BeaconEntityMapper {

    public BeaconEntity map(String name) {
        BeaconEntity entity = new BeaconEntity();
        entity.name = name;
        entity.userId = MyUser.getUid();
        return entity;
    }
}
