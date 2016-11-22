package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;
import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteModel;

import android.support.annotation.NonNull;

public final class FavoriteModelMapper {

    @NonNull
    public FavoriteModel map(@NonNull ItemsEntity itemsEntity, @NonNull FavoritesEntity favoritesEntity) {
        FavoriteModel model = new FavoriteModel();
        model.mId = itemsEntity.key;
        model.mName = itemsEntity.name;
        model.mImageUri = itemsEntity.imageUri;
        model.mAddedTime = favoritesEntity.addedTime;
        return model;
    }
}
