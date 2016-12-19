package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.ConfigEntity;

import rx.Single;

public interface FirebaseConfigsRepository {

    Single<ConfigEntity> find();
}
