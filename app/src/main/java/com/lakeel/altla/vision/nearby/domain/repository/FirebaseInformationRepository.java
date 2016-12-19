package com.lakeel.altla.vision.nearby.domain.repository;

import rx.Completable;

public interface FirebaseInformationRepository {

    Completable save(String userId, String title, String message);
}
