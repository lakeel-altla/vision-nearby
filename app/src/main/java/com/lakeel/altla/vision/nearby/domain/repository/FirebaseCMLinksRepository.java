package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.CMLinkEntity;

import rx.Single;

public interface FirebaseCMLinksRepository {

    Single<String> findCmJidByItemId(String itemId);

    Single<CMLinkEntity> findCmLinks();

    Single<String> saveCMApiKey(String apiKey);

    Single<String> saveCMSecretKey(String secretKey);

    Single<String> saveCMJid(String jid);
}
