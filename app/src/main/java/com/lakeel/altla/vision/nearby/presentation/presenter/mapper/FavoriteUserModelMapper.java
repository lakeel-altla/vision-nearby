package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;
import com.lakeel.altla.vision.nearby.domain.model.Presence;
import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;

public final class FavoriteUserModelMapper {

    public FavoriteUserModel map(User user) {
        FavoriteUserModel model = new FavoriteUserModel();
        model.userId = user.userId;
        model.userName = user.name;
        model.email = user.email;
        model.imageUri = user.imageUri;
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
