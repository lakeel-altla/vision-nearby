package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.cm.CmApplication;
import com.lakeel.altla.cm.CmRestClient;
import com.lakeel.altla.cm.data.MessagePostData;
import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.data.mapper.CmFavoritePostDataMapper;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;

import javax.inject.Inject;

import rx.Single;

public final class RestCmRepositoryImpl implements RestCmRepository {

    private CmFavoritePostDataMapper mapper = new CmFavoritePostDataMapper();

    @Inject
    RestCmRepositoryImpl() {
    }

    @Override
    public Single<Timestamp> saveFavorites(CmFavoriteData data) {
        CmRestClient client = CmApplication.getInstance().getClient();
        return client.getFavoritesService().saveFavorites(mapper.map(data));
    }

    @Override
    public Single<Timestamp> sendMessage(String jid, String message) {
        CmRestClient client = CmApplication.getInstance().getClient();

        MessagePostData postData = new MessagePostData();
        postData.body = message;

        return client.getMessageService().sendMessage(jid, postData);
    }
}
