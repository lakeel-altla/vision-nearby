package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CmLinkModel;

public final class CmLinksModelMapper {

    public CmLinkModel map(CmLinkEntity entity) {
        CmLinkModel model = new CmLinkModel();
        model.mApiKey = entity.apiKey;
        model.mSecretKey = entity.secretKey;
        model.mJid = entity.jid;
        return model;
    }
}
