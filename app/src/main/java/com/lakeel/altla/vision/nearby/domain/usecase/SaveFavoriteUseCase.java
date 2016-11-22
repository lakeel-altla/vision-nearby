package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoriteRepository;

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
