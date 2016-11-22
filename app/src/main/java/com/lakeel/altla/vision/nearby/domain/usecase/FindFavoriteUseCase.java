package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoriteRepository;
import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;

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
