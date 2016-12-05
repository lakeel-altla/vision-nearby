package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;

import java.util.Collections;
import java.util.List;

public final class CmFavoritesDataMapper {

    private static final String DEFAULT_VERSION = "0";

    private static final String DEFAULT_GROUP_NAME = "Demo";

    public CmFavoriteData map(String contactJid) {
        CmFavoriteData data = new CmFavoriteData();
        data.version = DEFAULT_VERSION;
        data.groups = Collections.singletonList(DEFAULT_GROUP_NAME);
        data.contactIds = Collections.singletonList(contactJid);
        return data;
    }

    public CmFavoriteData map(List<String> cmUserIds) {
        CmFavoriteData data = new CmFavoriteData();
        data.version = DEFAULT_VERSION;
        data.groups = Collections.singletonList(DEFAULT_GROUP_NAME);
        data.contactIds = cmUserIds;
        return data;
    }
}
