package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.ConfigsEntity;

import rx.Single;

public interface FirebaseConfigsRepository {

    Single<ConfigsEntity> find();
}
