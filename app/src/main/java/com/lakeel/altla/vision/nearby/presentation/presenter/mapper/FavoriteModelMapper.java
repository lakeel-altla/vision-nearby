package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(UserProfile userProfile) {
        FavoriteModel model = new FavoriteModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
