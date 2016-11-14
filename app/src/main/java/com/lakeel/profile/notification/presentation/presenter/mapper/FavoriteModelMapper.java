package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.FavoritesEntity;
import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.presentation.presenter.model.FavoriteModel;

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
