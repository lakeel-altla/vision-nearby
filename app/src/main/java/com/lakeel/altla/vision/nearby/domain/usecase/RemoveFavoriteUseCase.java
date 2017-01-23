package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository repository;

    @Inject
    RemoveFavoriteUseCase() {
    }

    public Completable execute(String favoriteUserId) {
        String userId = MyUser.getUserId();
        return repository.removeFavorite(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
