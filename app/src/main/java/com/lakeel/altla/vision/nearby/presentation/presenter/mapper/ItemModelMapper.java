package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;

public final class ItemModelMapper {

    public ItemModel map(UserEntity entity) {
        ItemModel model = new ItemModel();
        model.mId = entity.key;
        model.mName = entity.name;
        model.mImageUri = entity.imageUri;
        model.mEmail = entity.email;
        return model;
    }
}
