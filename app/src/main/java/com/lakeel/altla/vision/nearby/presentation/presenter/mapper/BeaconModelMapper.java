package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconModel;

public final class BeaconModelMapper {

    public BeaconModel map(BeaconEntity entity) {
        BeaconModel model = new BeaconModel();
        model.mBeaconId = entity.key;
        model.mName = entity.name;
        return model;
    }
}
