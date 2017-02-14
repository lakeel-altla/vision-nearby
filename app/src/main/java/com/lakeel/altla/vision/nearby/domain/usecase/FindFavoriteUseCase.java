package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindFavoriteUseCase {

    @Inject
    UserFavoriteRepository repository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<Favorite> execute(String favoriteUserId) {
        String userId = CurrentUser.getUid();
        return repository.find(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
