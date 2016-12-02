package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class BeaconsEntityMapper {

    public BeaconsEntity map(String name) {
        BeaconsEntity entity = new BeaconsEntity();
        entity.name = name;
        entity.userId = MyUser.getUid();
        return entity;
    }
}
