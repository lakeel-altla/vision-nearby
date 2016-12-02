package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;

import javax.inject.Inject;

import rx.Single;

public final class FindFavoriteUseCase {

    @Inject
    FirebaseFavoritesRepository mFirebaseFavoritesRepository;

    @Inject
    FindFavoriteUseCase() {
    }

    public Single<FavoriteEntity> execute(String id) {
        return mFirebaseFavoritesRepository.findFavoriteById(id);
    }
}
