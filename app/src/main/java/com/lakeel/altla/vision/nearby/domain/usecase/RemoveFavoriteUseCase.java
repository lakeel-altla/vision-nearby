package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveFavoriteUseCase {

    @Inject
    UserFavoriteRepository repository;

    @Inject
    RemoveFavoriteUseCase() {
    }

    public Completable execute(@NonNull String favoriteUserId) {
        String userId = CurrentUser.getUid();
        return repository.remove(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
