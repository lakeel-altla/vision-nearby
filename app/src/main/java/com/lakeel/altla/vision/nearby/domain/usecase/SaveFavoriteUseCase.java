package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserFavoriteRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveFavoriteUseCase {

    @Inject
    FirebaseUserFavoriteRepository repository;

    @Inject
    SaveFavoriteUseCase() {
    }

    public Completable execute(String favoriteUserId) {
        String myUserId = MyUser.getUserId();
        return repository.saveFavorite(myUserId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
