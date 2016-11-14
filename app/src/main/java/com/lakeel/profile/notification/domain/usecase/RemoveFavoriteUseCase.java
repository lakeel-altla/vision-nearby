package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseFavoriteRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveFavoriteUseCase {

    @Inject
    FirebaseFavoriteRepository mFavoritesRepository;

    @Inject
    public RemoveFavoriteUseCase() {
    }

    public Completable execute(String uid) {
        return mFavoritesRepository.removeFavoriteByUid(uid);
    }
}
