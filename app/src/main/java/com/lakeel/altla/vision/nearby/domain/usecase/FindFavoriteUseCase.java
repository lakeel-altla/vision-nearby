package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindFavoriteUseCase {

    @Inject
    FirebaseUserFavoriteRepository repository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<Favorite> execute(String favoriteUserId) {
        String myUserId = MyUser.getUserId();
        return repository.findFavorite(myUserId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
