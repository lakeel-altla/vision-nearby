package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;

public final class NearbyItemsModelMapper {

    public NearbyItemModel map(UserEntity entity) {
        NearbyItemModel model = new NearbyItemModel();
        model.userId = entity.key;
        model.name = entity.name;
        model.imageUri = entity.imageUri;
        return model;
    }
}
