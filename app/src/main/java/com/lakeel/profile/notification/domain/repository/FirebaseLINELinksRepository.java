package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.LINELinksEntity;

import rx.Single;

public interface FirebaseLINELinksRepository {

    Single<String> saveUrl(String url);

    Single<LINELinksEntity> findByUserId(String userId);
}
