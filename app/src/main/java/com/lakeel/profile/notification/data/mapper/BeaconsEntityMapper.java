package com.lakeel.profile.notification.data.mapper;

import com.lakeel.profile.notification.data.entity.BeaconsEntity;

public final class BeaconsEntityMapper {

    public BeaconsEntity map(String name) {
        BeaconsEntity entity = new BeaconsEntity();
        entity.name = name;
        return entity;
    }
}
