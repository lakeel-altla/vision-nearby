package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;

public final class LineLinkEntityMapper {

    public LineLinkEntity map(String url) {
        LineLinkEntity entity = new LineLinkEntity();
        entity.url = url;
        return entity;
    }
}
