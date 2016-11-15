package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.PreferencesEntity;
import com.lakeel.profile.notification.presentation.presenter.model.PreferenceModel;

public final class PreferencesModelMapper {

    public PreferenceModel map(PreferencesEntity entity) {
        PreferenceModel model = new PreferenceModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        model.mPublishInBackground = entity.isPublishInBackground;
        model.mSubscribeInBackground = entity.isSubscribeInBackground;
        return model;
    }
}
