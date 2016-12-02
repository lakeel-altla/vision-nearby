package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;

import javax.inject.Inject;

import rx.Completable;

public final class RemoveFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository mFavoritesRepository;

    @Inject
    public RemoveFavoriteUseCase() {
    }

    public Completable execute(String uid) {
        return mFavoritesRepository.removeFavoriteByUid(uid);
    }
}
