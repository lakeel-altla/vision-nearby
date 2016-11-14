package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.FavoritesEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseFavoriteRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveFavoriteUseCase {

    @Inject
    FirebaseFavoriteRepository mFirebaseFavoriteRepository;

    @Inject
    public SaveFavoriteUseCase() {
    }

    public Single<FavoritesEntity> execute(String id) {
        return mFirebaseFavoriteRepository.saveFavorite(id);
    }
}
