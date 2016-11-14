package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.PreferencesEntity;
import com.lakeel.profile.notification.presentation.presenter.model.PreferencesModel;

public final class PreferencesModelMapper {

    public PreferencesModel map(PreferencesEntity entity) {
        PreferencesModel model = new PreferencesModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        model.mPublishInBackground = entity.isPublishInBackground;
        model.mSubscribeInBackground = entity.isSubscribeInBackground;
        return model;
    }
}
