package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public final class DeviceModelMapper {

    public DeviceModel map(Beacon beacon) {
        DeviceModel model = new DeviceModel();
        model.beaconId = beacon.beaconId;
        model.name = beacon.name;
        model.lastUsedTime = beacon.lastUsedTime;
        model.isLost = beacon.isLost;
        return model;
    }
}
