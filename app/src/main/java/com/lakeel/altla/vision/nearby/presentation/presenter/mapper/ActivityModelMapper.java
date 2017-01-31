package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public final class ActivityModelMapper {

    public ActivityModel map(UserProfile userProfile) {
        ActivityModel model = new ActivityModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.email = userProfile.email;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
