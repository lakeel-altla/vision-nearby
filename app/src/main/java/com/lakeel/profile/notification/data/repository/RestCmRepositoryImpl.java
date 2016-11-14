package com.lakeel.profile.notification.data.repository;

import com.lakeel.altla.cm.CMApiClient;
import com.lakeel.altla.cm.CMApplication;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.profile.notification.data.mapper.CmFavoritesPostDataMapper;
import com.lakeel.profile.notification.domain.model.CmFavoritesData;
import com.lakeel.profile.notification.domain.repository.RestCmRepository;

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
