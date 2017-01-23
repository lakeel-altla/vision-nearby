package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PreferenceModel;

public final class PreferencesModelMapper {

    public PreferenceModel map(PreferenceEntity entity) {
        PreferenceModel model = new PreferenceModel();
        model.beaconId = entity.beaconId;
        model.isAdvertiseInBackgroundEnabled = entity.isAdvertiseInBackgroundEnabled;
        model.isSubscribeInBackgroundEnabled = entity.isSubscribeInBackgroundEnabled;
        return model;
    }
}
