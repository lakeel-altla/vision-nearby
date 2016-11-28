package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.FavoritesEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoriteRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindFavoritesUseCase {

    @Inject
    FirebaseFavoriteRepository mFavoritesRepository;

    @Inject
    FindFavoritesUseCase() {
    }

    public Observable<FavoritesEntity> execute() {
        return mFavoritesRepository.findFavorites();
    }
}
