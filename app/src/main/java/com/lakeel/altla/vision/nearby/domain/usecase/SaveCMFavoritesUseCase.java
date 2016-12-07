package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmFavoritesUseCase {

    @Inject
    RestCmRepository repository;

    @Inject
    SaveCmFavoritesUseCase() {
    }

    public Single<Timestamp> execute(CmFavoriteData data) {
        return repository.saveFavorites(data);
    }

}
