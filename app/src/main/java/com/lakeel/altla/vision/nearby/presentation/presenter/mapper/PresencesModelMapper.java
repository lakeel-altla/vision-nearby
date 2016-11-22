package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.data.entity.PresencesEntity;

public final class PresencesModelMapper {

    public PresenceModel map(PresencesEntity entity) {
        PresenceModel model = new PresenceModel();
        model.mConnected = entity.isConnected;
        model.mLastOnline = entity.lastOnline;
        return model;
    }

}
