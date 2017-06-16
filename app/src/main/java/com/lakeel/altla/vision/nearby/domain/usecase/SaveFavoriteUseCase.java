package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveFavoriteUseCase {

    @Inject
    UserFavoriteRepository repository;

    @Inject
    SaveFavoriteUseCase() {
    }

    public Completable execute(@NonNull String favoriteUserId) {
        Favorite favorite = new Favorite();
        favorite.userId = CurrentUser.getUid();
        favorite.favoriteUserId = favoriteUserId;
        return repository.save(favorite).subscribeOn(Schedulers.io());
    }
}
