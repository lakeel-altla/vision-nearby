package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconModel;

public final class BeaconModelMapper {

    public BeaconModel map(BeaconsEntity entity) {
        BeaconModel model = new BeaconModel();
        model.mBeaconId = entity.key;
        model.mName = entity.name;
        return model;
    }
}
