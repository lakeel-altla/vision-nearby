package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public final class ActivityModelMapper {

    public ActivityModel map(UserEntity entity) {
        ActivityModel model = new ActivityModel();
        model.userId = entity.userId;
        model.userName = entity.name;
        model.email = entity.email;
        model.imageUri = entity.imageUri;
        return model;
    }
}
