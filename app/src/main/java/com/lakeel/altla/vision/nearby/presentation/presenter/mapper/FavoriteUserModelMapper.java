package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;

public final class FavoriteUserModelMapper {

    public FavoriteUserModel map(UserEntity entity) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.userId = entity.userId;
        model.userName = entity.name;
        model.email = entity.email;
        model.imageUri = entity.imageUri;
        return model;
    }

    public FavoriteUserModel map(PresenceEntity entity) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.isConnected = entity.isConnected;
        model.lastOnlineTime = entity.lastOnlineTime;
        return model;
    }

    public FavoriteUserModel map(LineLinkEntity entity) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.lineUrl = entity.url;
        return model;
    }
}
