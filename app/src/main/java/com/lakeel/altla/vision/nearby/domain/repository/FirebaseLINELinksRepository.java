package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;

import rx.Single;

public interface FirebaseLINELinksRepository {

    Single<String> saveUrl(String userId, String url);

    Single<LineLinkEntity> findByUserId(String userId);

    Single<LineLinkEntity> findUserIdByLineUrl(String url);
}
