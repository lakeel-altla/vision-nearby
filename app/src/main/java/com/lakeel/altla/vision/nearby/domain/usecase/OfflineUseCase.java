package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresencesRepository;

import javax.inject.Inject;

import rx.Completable;

public final class OfflineUseCase {

    @Inject
    FirebasePresencesRepository repository;

    @Inject
    OfflineUseCase() {
    }

    public Completable execute(String userId) {
        return repository.savePresenceOffline(userId);
    }
}
