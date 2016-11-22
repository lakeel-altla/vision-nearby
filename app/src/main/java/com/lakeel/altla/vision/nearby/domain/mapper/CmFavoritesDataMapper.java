package com.lakeel.altla.vision.nearby.domain.mapper;

import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;

import java.util.Collections;
import java.util.List;

public final class CmFavoritesDataMapper {

    private static final String DEFAULT_VERSION = "0";

    private static final String DEFAULT_GROUP_NAME = "Demo";

    public CmFavoritesData map(String contactJid) {
        CmFavoritesData data = new CmFavoritesData();
        data.mVersion = DEFAULT_VERSION;
        data.mGroups = Collections.singletonList(DEFAULT_GROUP_NAME);
        data.mContactIds = Collections.singletonList(contactJid);
        return data;
    }

    public CmFavoritesData map(List<String> cmUserIds) {
        CmFavoritesData data = new CmFavoritesData();
        data.mVersion = DEFAULT_VERSION;
        data.mGroups = Collections.singletonList(DEFAULT_GROUP_NAME);
        data.mContactIds = cmUserIds;
        return data;
    }
}
