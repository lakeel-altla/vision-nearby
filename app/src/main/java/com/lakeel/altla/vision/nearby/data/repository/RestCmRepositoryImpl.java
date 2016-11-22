package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.cm.CMApiClient;
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.data.mapper.CmFavoritesPostDataMapper;
import com.lakeel.altla.vision.nearby.domain.model.CmFavoritesData;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Observable;

public final class RestCmRepositoryImpl implements RestCmRepository {

    private CmFavoritesPostDataMapper mMapper = new CmFavoritesPostDataMapper();

    @Inject
    RestCmRepositoryImpl() {
    }

    @Override
    public Observable<Timestamp> saveFavorites(CmFavoritesData data) {
        CMApiClient client = CMApplication.getInstance().getClient();
        return client.getFavoritesService().saveFavorites(mMapper.map(data));
    }
}
