package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.CMLinksEntity;
import com.lakeel.profile.notification.presentation.presenter.model.CMLinksModel;

public final class CMLinksModelMapper {

    public CMLinksModel map(CMLinksEntity entity) {
        CMLinksModel model = new CMLinksModel();
        model.mApiKey = entity.apiKey;
        model.mSecretKey = entity.secretKey;
        model.mJid = entity.jid;
        return model;
    }
}
