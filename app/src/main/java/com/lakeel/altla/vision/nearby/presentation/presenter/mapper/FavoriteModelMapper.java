package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(@NonNull FavoritesEntity favoritesEntity, @NonNull UserEntity userEntity) {
        FavoriteModel model = new FavoriteModel();
        model.mId = userEntity.key;
        model.mName = userEntity.name;
        model.mImageUri = userEntity.imageUri;
        model.mAddedTime = favoritesEntity.addedTime;
        return model;
    }
}
