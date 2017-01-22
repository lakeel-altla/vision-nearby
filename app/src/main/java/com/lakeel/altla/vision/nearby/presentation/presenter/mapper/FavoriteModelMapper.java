package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(@NonNull UserEntity userEntity) {
        FavoriteModel model = new FavoriteModel();
        model.userId = userEntity.userId;
        model.userName = userEntity.name;
        model.imageUri = userEntity.imageUri;
        return model;
    }
}
