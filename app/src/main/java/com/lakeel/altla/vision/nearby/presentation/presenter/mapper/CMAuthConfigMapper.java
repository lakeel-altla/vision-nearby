package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.cm.config.AuthConfig;
import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;

public final class CMAuthConfigMapper {

    public AuthConfig map(CMLinkEntity entity) {
        return new AuthConfig(entity.apiKey, entity.secretKey);
    }
}
