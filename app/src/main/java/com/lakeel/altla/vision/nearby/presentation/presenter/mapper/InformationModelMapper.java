package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public final class InformationModelMapper {

    public InformationModel map(InformationEntity entity) {
        InformationModel model = new InformationModel();
        model.informationId = entity.informationId;
        model.title = entity.title;
        model.body = entity.body;
        model.postTime = entity.postTime;
        return model;
    }
}
