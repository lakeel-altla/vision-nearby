package com.lakeel.altla.vision.nearby.domain.repository;

import rx.Single;

public interface FirebaseTokensRepository {

    Single<String> saveToken(String token);
}
