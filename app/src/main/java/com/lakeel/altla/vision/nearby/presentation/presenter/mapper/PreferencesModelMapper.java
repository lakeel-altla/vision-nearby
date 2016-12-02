package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PreferenceModel;

public final class PreferencesModelMapper {

    public PreferenceModel map(PreferenceEntity entity) {
        PreferenceModel model = new PreferenceModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        model.mPublishInBackgroundEnabled = entity.isPublishInBackgroundEnabled;
        model.mSubscribeInBackgroundEnabled = entity.isSubscribeInBackgroundEnabled;
        return model;
    }
}
