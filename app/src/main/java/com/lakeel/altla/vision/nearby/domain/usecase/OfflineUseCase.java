package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionsRepository;

import javax.inject.Inject;

import rx.Completable;

public final class OfflineUseCase {

    @Inject
    FirebaseConnectionsRepository repository;

    @Inject
    OfflineUseCase() {
    }

    public Completable execute(String userId) {
        return repository.saveOffline(userId);
    }
}
