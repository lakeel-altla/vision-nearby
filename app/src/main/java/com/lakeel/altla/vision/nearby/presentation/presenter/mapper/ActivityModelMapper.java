package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public final class ActivityModelMapper {

    public ActivityModel map(User user) {
        ActivityModel model = new ActivityModel();
        model.userId = user.userId;
        model.userName = user.name;
        model.email = user.email;
        model.imageUri = user.imageUri;
        return model;
    }
}
