package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.PreferencesEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PreferenceModel;

public final class PreferencesModelMapper {

    public PreferenceModel map(PreferencesEntity entity) {
        PreferenceModel model = new PreferenceModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        model.mPublishInBackgroundEnabled = entity.isPublishInBackgroundEnabled;
        model.mSubscribeInBackgroundEnabled = entity.isSubscribeInBackgroundEnabled;
        return model;
    }
}
