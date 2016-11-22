package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public final class DeviceModelMapper {

    public DeviceModel map(BeaconsEntity entity) {
        DeviceModel model = new DeviceModel();
        model.mBeaconId = entity.key;
        model.mName = entity.name;
        return model;
    }
}
