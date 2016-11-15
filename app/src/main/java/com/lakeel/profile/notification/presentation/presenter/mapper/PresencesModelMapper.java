package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.presentation.presenter.model.PresenceModel;
import com.lakeel.profile.notification.data.entity.PresencesEntity;

public final class PresencesModelMapper {

    public PresenceModel map(PresencesEntity entity) {
        PresenceModel model = new PresenceModel();
        model.mConnected = entity.isConnected;
        model.mLastOnline = entity.lastOnline;
        return model;
    }

}
