package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.LineLinkEntity;

import rx.Single;

public interface FirebaseLineLinksRepository {

    Single<String> saveUrl(String url);

    Single<LineLinkEntity> findByUserId(String userId);

    Single<LineLinkEntity> findUserIdByLineUrl(String url);
}
