package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.TokenEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseTokensRepository {

    Single<String> saveToken(String userId, String beaconId, String token);

    Single<String> findTokenByUserId(String userId);

    Observable<TokenEntity> findTokensByUserId(String userId);
}
