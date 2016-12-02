package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.cm.CMRestClient;
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.data.mapper.CmFavoritePostDataMapper;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Observable;

public final class RestCmRepositoryImpl implements RestCmRepository {

    private CmFavoritePostDataMapper mMapper = new CmFavoritePostDataMapper();

    @Inject
    RestCmRepositoryImpl() {
    }

    @Override
    public Observable<Timestamp> saveFavorites(CmFavoritesData data) {
        CMRestClient client = CMApplication.getInstance().getClient();
        return client.getFavoritesService().saveFavorites(mMapper.map(data));
    }
}
