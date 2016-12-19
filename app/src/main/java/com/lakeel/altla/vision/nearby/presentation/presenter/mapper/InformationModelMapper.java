package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.InformationEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public final class InformationModelMapper {

    public InformationModel map(InformationEntity entity) {
        InformationModel model = new InformationModel();
        model.title = entity.title;
        model.message = entity.message;
        model.postTime = entity.postTime;
        return model;
    }
}
