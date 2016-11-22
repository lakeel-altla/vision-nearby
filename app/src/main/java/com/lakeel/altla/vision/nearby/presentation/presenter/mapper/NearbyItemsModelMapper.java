package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;

public final class NearbyItemsModelMapper {

    public NearbyItemModel map(ItemsEntity entity) {
        NearbyItemModel model = new NearbyItemModel();
        model.mId = entity.key;
        model.mName = entity.name;
        model.mImageUri = entity.imageUri;
        return model;
    }
}
