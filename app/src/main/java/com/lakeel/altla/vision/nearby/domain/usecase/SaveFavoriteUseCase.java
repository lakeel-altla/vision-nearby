package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;

import javax.inject.Inject;

import rx.Completable;

public final class SaveFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository repository;

    @Inject
    SaveFavoriteUseCase() {
    }

    public Completable execute(String myUserId, String otherUserId) {
        return repository.saveFavorite(myUserId, otherUserId);
    }
}
