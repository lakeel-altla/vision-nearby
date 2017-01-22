package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.LineLinkEntity;

public final class LineLinkEntityMapper {

    public LineLinkEntity map(String url) {
        LineLinkEntity entity = new LineLinkEntity();
        entity.url = url;
        return entity;
    }
}
