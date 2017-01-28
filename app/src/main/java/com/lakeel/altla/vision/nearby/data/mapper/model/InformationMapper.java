package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.domain.model.Information;

public final class InformationMapper {

    public Information map(InformationEntity entity, String key) {
        Information information = new Information();
        information.informationId = key;
        information.title = entity.title;
        information.body = entity.body;
        information.postTime = entity.postTime;
        return information;
    }
}
