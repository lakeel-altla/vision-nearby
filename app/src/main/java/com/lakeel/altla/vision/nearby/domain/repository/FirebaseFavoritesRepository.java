package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseFavoritesRepository {

    Observable<FavoriteEntity> findFavoritesByUserId(String userId);

    Single<FavoriteEntity> findFavorite(String myUserId, String otherUserId);

    Single<FavoriteEntity> saveFavorite(String myUserId, String otherUserId);

    Completable removeFavoriteByUid(String myUserId, String otherUserId);
}
