package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseItemsRepository {

    Single<ItemsEntity> findItemsById(String id);

    Single<ItemsEntity> findItemsByName(String name);

    Completable saveItem();

    Single<String> saveBeacon(String userId, String beaconId);

    Observable<String> findBeaconsByUserId(String userId);
}
