package com.lakeel.altla.vision.nearby.domain.repository;

import rx.Completable;

public interface FirebaseAuthRepository {

    Completable signOut();
}
