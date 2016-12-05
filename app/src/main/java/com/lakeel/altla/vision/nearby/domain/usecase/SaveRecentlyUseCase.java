package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveRecentlyUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveRecentlyUseCase() {
    }

    public Single<String> execute(String myUserId, String otherUserId) {
        return repository.saveHistory(myUserId, otherUserId);
    }
}
