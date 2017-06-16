package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.model.Information;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public final class InformationModelMapper {

    private InformationModelMapper() {
    }

    public static InformationModel map(@NonNull Information information) {
        InformationModel model = new InformationModel();
        model.informationId = information.informationId;
        model.title = information.title;
        model.body = information.body;
        model.postTime = (Long) information.postTime;
        return model;
    }
}
