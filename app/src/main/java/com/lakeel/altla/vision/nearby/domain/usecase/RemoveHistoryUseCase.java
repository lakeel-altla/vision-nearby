package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Completable;

public final class RemoveHistoryUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    RemoveHistoryUseCase() {
    }

    public Completable execute(String userId, String uniqueKey) {
        return repository.removeByUniqueKey(userId, uniqueKey);
    }
}
