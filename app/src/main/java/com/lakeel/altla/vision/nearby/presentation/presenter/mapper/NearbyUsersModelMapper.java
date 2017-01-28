package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;

public final class NearbyUsersModelMapper {

    public NearbyUserModel map(User user) {
        NearbyUserModel model = new NearbyUserModel();
        model.userId = user.userId;
        model.userName = user.name;
        model.imageUri = user.imageUri;
        return model;
    }
}
