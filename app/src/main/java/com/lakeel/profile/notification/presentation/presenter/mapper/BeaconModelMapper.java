package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.BeaconIdEntity;
import com.lakeel.profile.notification.presentation.presenter.model.BeaconModel;

public final class BeaconModelMapper {

    public BeaconModel map(BeaconIdEntity entity) {
        BeaconModel model = new BeaconModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        return model;
    }
}
