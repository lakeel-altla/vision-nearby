package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceBeaconIdEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconIdModel;

public final class BeaconIdModelMapper {

    public BeaconIdModel map(PreferenceBeaconIdEntity entity) {
        BeaconIdModel model = new BeaconIdModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        return model;
    }
}
