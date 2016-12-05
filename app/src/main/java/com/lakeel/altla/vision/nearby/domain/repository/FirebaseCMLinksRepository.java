package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;

import rx.Single;

public interface FirebaseCMLinksRepository {

    Single<String> findCmJidByItemId(String userId);

    Single<CMLinkEntity> findCmLinksByUserId(String userId);

    Single<String> saveCMApiKey(String userId, String apiKey);

    Single<String> saveCMSecretKey(String userId, String secretKey);

    Single<String> saveCMJid(String userId, String jid);
}
