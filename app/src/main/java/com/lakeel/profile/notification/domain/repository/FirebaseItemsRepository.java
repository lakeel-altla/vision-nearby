package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.ItemsEntity;

import rx.Completable;
import rx.Single;

public interface FirebaseItemsRepository {

    Single<ItemsEntity> findItemsById(String id);

    Single<ItemsEntity> findItemsByName(String name);

    Completable saveItem();

    Single<String> saveBeaconId(String beaconId);
}
