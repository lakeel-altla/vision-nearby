package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CMLinksModel;

public final class CMLinksModelMapper {

    public CMLinksModel map(CMLinkEntity entity) {
        CMLinksModel model = new CMLinksModel();
        model.mApiKey = entity.apiKey;
        model.mSecretKey = entity.secretKey;
        model.mJid = entity.jid;
        return model;
    }
}
