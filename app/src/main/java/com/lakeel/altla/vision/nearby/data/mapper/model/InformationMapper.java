package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.google.firebase.database.DataSnapshot;
import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.domain.model.Information;

public final class InformationMapper {

    public Information map(DataSnapshot snapshot) {
        InformationEntity entity = snapshot.getValue(InformationEntity.class);

        Information information = new Information();
        information.informationId = snapshot.getKey();
        information.title = entity.title;
        information.body = entity.body;
        information.postTime = (Long) entity.postTime;

        return information;
    }
}
