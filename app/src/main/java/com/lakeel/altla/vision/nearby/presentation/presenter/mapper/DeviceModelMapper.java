package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public final class DeviceModelMapper {

    public DeviceModel map(BeaconEntity entity) {
        DeviceModel model = new DeviceModel();
        model.beaconId = entity.beaconId;
        model.name = entity.name;
        model.lastUsedTime = entity.lastUsedTime;
        model.isLost = entity.isLost;
        return model;
    }
}
