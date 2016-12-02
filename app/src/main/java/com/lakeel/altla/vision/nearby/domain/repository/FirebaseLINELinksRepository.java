package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.LINELinkEntity;

import rx.Single;

public interface FirebaseLINELinksRepository {

    Single<String> saveUrl(String url);

    Single<LINELinkEntity> findByUserId(String userId);

    Single<LINELinkEntity> findUserIdByLINEUrl(String url);
}
