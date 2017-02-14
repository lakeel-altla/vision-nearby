package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;

public final class NearbyUsersModelMapper {

    private NearbyUsersModelMapper() {
    }

    public static NearbyUserModel map(UserProfile userProfile) {
        NearbyUserModel model = new NearbyUserModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
