package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.CMLinksEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CMLinksModel;

public final class CMLinksModelMapper {

    public CMLinksModel map(CMLinksEntity entity) {
        CMLinksModel model = new CMLinksModel();
        model.mApiKey = entity.apiKey;
        model.mSecretKey = entity.secretKey;
        model.mJid = entity.jid;
        return model;
    }
}
