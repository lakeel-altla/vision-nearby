package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.CMRestClient;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.data.mapper.CmFavoritePostDataMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Single;

public final class RestCmRepositoryImpl implements RestCmRepository {

    private CmFavoritePostDataMapper mapper = new CmFavoritePostDataMapper();

    @Inject
    RestCmRepositoryImpl() {
    }

    @Override
    public Single<Timestamp> saveFavorites(CmFavoriteData data) {
        CMRestClient client = CMApplication.getInstance().getClient();
        return client.getFavoritesService().saveFavorites(mapper.map(data));
    }
}
