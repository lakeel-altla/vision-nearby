package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;

public final class NearbyUsersModelMapper {

    public NearbyUserModel map(UserEntity entity) {
        NearbyUserModel model = new NearbyUserModel();
        model.userId = entity.userId;
        model.userName = entity.name;
        model.imageUri = entity.imageUri;
        return model;
    }
}
