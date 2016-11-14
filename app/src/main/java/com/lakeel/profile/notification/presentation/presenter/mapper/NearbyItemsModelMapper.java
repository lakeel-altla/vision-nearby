package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.presentation.presenter.model.NearbyItemsModel;
import com.lakeel.profile.notification.data.entity.ItemsEntity;

public final class NearbyItemsModelMapper {

    public NearbyItemsModel map(ItemsEntity entity) {
        NearbyItemsModel model = new NearbyItemsModel();
        model.mId = entity.key;
        model.mName = entity.name;
        model.mImageUri = entity.imageUri;
        return model;
    }
}
