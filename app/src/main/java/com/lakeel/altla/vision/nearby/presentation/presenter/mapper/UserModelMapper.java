package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;

public final class UserModelMapper {

    public UserModel map(UserEntity entity) {
        UserModel model = new UserModel();
        model.userId = entity.userId;
        model.userName = entity.name;
        model.imageUri = entity.imageUri;
        model.email = entity.email;
        return model;
    }
}
