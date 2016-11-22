package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.CMLinksEntity;

import rx.Single;

public interface FirebaseCMLinksRepository {

    Single<String> findCmJidByItemId(String itemId);

    Single<CMLinksEntity> findCmLinks();

    Single<String> saveCMApiKey(String apiKey);

    Single<String> saveCMSecretKey(String secretKey);

    Single<String> saveCMJid(String jid);
}
