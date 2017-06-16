package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    private FavoriteModelMapper() {
    }

    public static FavoriteModel map(@NonNull UserProfile userProfile) {
        FavoriteModel model = new FavoriteModel();
        model.userId = userProfile.userId;
        model.userName = userProfile.name;
        model.imageUri = userProfile.imageUri;
        return model;
    }
}
