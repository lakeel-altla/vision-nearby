package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository repository;

    @Inject
    public SaveFavoriteUseCase() {
    }

    public Single<FavoriteEntity> execute(String myUserId, String otherUserId) {
        return repository.saveFavorite(myUserId, otherUserId);
    }
}
