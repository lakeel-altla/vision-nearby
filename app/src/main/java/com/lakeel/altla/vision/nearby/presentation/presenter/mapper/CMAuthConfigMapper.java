package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.cm.config.AuthConfig;
import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;

public final class CmAuthConfigMapper {

    public AuthConfig map(CmLinkEntity entity) {
        return new AuthConfig(entity.apiKey, entity.secretKey);
    }
}
