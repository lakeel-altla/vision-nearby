package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.altla.cm.config.AuthConfig;
import com.lakeel.profile.notification.data.entity.CMLinksEntity;

public final class CMAuthConfigMapper {

    public AuthConfig map(CMLinksEntity entity) {
        return new AuthConfig(entity.apiKey, entity.secretKey);
    }
}
