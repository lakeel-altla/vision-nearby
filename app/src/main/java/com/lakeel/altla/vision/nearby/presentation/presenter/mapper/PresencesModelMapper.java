package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.domain.entity.PresenceEntity;

public final class PresencesModelMapper {

    public PresenceModel map(PresenceEntity entity) {
        PresenceModel model = new PresenceModel();
        model.isConnected = entity.isConnected;
        model.lastOnlineTime = entity.lastOnlineTime;
        return model;
    }

}
