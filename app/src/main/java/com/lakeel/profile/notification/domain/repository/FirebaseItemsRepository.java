package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.ItemsEntity;

import rx.Single;

public interface FirebaseItemsRepository {

    Single<ItemsEntity> findItemsById(String id);

    Single<ItemsEntity> findItemsByName(String name);

    Single<ItemsEntity> saveItem();
}
