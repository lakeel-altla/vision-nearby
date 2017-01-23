package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.domain.entity.FavoriteEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseFavoritesRepository {

    Observable<FavoriteEntity> findFavoritesByUserId(String myUserId);

    Single<FavoriteEntity> findFavorite(String myUserId, String favoriteUserId);

    Completable saveFavorite(String myUserId, String favoriteUserId);

    Completable removeFavorite(String myUserId, String favoriteUserId);
}
