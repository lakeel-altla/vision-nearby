package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.domain.model.Presence;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;

public final class FavoriteUserModelMapper {

    public FavoriteUserModel map(UserProfile userProfile) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.email = userProfile.email;
        model.imageUri = userProfile.imageUri;
        return model;
    }

    public FavoriteUserModel map(Presence presence) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.isConnected = presence.isConnected;
        model.lastOnlineTime = presence.lastOnlineTime;
        return model;
    }

    public FavoriteUserModel map(LineLinkEntity entity) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.lineUrl = entity.url;
        return model;
    }
}
