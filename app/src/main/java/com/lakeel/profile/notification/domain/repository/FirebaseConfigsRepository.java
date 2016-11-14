package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.ConfigsEntity;

import rx.Single;

public interface FirebaseConfigsRepository {

    Single<ConfigsEntity> find();
}
