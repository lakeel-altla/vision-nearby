package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;

public final class BeaconsEntityMapper {

    public BeaconsEntity map(String name) {
        BeaconsEntity entity = new BeaconsEntity();
        entity.name = name;
        return entity;
    }
}
