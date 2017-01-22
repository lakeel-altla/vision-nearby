package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.FavoriteEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository repository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<FavoriteEntity> execute(String favoriteUserId) {
        String myUserId = MyUser.getUserId();
        return repository.findFavorite(myUserId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
