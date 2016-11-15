package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.presentation.presenter.model.NearbyItemModel;
import com.lakeel.profile.notification.data.entity.ItemsEntity;

public final class NearbyItemsModelMapper {

    public NearbyItemModel map(ItemsEntity entity) {
        NearbyItemModel model = new NearbyItemModel();
        model.mId = entity.key;
        model.mName = entity.name;
        model.mImageUri = entity.imageUri;
        return model;
    }
}
