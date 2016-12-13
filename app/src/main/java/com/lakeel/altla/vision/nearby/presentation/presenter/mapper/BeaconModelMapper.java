package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public final class BeaconModelMapper {

    public DeviceModel map(BeaconEntity entity) {
        DeviceModel model = new DeviceModel();
        model.beaconId = entity.key;
        model.name = entity.name;
        model.lastUsedTime = entity.lastUsedTime;
        return model;
    }
}
