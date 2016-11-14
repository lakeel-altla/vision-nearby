package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.presentation.presenter.model.PresencesModel;
import com.lakeel.profile.notification.data.entity.PresencesEntity;

public final class PresencesModelMapper {

    public PresencesModel map(PresencesEntity entity) {
        PresencesModel model = new PresencesModel();
        model.mConnected = entity.isConnected;
        model.mLastOnline = entity.lastOnline;
        return model;
    }

}
