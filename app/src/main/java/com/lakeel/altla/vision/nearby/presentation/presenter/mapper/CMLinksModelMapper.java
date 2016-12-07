package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.CmLinksModel;

public final class CmLinksModelMapper {

    public CmLinksModel map(CmLinkEntity entity) {
        CmLinksModel model = new CmLinksModel();
        model.mApiKey = entity.apiKey;
        model.mSecretKey = entity.secretKey;
        model.mJid = entity.jid;
        return model;
    }
}
