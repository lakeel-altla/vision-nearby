package com.lakeel.altla.vision.nearby.domain.repository;

import rx.Observable;
import rx.Single;

public interface FirebaseTokensRepository {

    Single<String> saveToken(String userId, String beaconId, String token);

    Single<String> findTokenByUserId(String userId);

    Observable<String> findTokensByUserId(String userId);

}
