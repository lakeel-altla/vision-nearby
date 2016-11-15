package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.BeaconsEntity;
import com.lakeel.profile.notification.presentation.presenter.model.DeviceModel;

public final class DeviceModelMapper {

    public DeviceModel map(BeaconsEntity entity) {
        DeviceModel model = new DeviceModel();
        model.mBeaconId = entity.key;
        model.mName = entity.name;
        return model;
    }
}
