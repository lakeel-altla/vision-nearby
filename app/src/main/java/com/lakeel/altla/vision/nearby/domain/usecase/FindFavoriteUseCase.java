package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindFavoriteUseCase {

    @Inject
    UserFavoriteRepository repository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<Favorite> execute(@NonNull String favoriteUserId) {
        String userId = CurrentUser.getUid();
        return repository.find(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
