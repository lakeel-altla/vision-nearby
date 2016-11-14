package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.FavoritesEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseFavoriteRepository {

    Observable<FavoritesEntity> findFavorites();

    Single<FavoritesEntity> findFavoriteById(String id);

    Single<FavoritesEntity> saveFavorite(String id);

    Completable removeFavoriteByUid(String id);
}
