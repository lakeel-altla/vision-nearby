package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.CmLinkEntity;

import rx.Single;

public interface FirebaseCmLinksRepository {

    Single<String> findJidByItemId(String userId);

    Single<CmLinkEntity> findCmLinksByUserId(String userId);

    Single<String> saveApiKey(String userId, String apiKey);

    Single<String> saveSecretKey(String userId, String secretKey);

    Single<String> saveJid(String userId, String jid);
}
