package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(User user) {
        FavoriteModel model = new FavoriteModel();
        model.userId = user.userId;
        model.userName = user.name;
        model.imageUri = user.imageUri;
        return model;
    }
}
