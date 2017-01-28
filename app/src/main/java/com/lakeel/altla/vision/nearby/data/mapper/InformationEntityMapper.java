package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;

public final class InformationEntityMapper {

    public InformationEntity map(String title, String message) {
        InformationEntity entity = new InformationEntity();
        entity.title = title;
        entity.body = message;
        return entity;
    }

}
