package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseFavoritesRepository {

    Observable<FavoriteEntity> findFavorites();

    Single<FavoriteEntity> findFavoriteById(String id);

    Single<FavoriteEntity> saveFavorite(String id);

    Completable removeFavoriteByUid(String id);
}
