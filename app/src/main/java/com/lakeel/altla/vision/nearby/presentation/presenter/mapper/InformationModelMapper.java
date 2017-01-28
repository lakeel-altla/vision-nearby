package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.Information;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public final class InformationModelMapper {

    public InformationModel map(Information information) {
        InformationModel model = new InformationModel();
        model.informationId = information.informationId;
        model.title = information.title;
        model.body = information.body;
        model.postTime = information.postTime;
        return model;
    }
}
