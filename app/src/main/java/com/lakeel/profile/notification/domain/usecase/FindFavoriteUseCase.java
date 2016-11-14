package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseFavoriteRepository;
import com.lakeel.profile.notification.data.entity.FavoritesEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindFavoriteUseCase {

    @Inject
    FirebaseFavoriteRepository mFirebaseFavoriteRepository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<FavoritesEntity> execute(String id) {
        return mFirebaseFavoriteRepository.findFavoriteById(id);
    }
}
