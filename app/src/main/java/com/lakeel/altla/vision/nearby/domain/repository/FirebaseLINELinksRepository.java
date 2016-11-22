package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.LINELinksEntity;

import rx.Single;

public interface FirebaseLINELinksRepository {

    Single<String> saveUrl(String url);

    Single<LINELinksEntity> findByUserId(String userId);
}
