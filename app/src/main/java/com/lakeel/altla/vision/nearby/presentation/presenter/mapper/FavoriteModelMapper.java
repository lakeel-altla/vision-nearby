package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(@NonNull FavoriteEntity favoriteEntity, @NonNull UserEntity userEntity) {
        FavoriteModel model = new FavoriteModel();
        model.mId = userEntity.key;
        model.mName = userEntity.name;
        model.mImageUri = userEntity.imageUri;
        model.mAddedTime = favoriteEntity.addedTime;
        return model;
    }
}
