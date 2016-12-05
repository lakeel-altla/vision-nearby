package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveRecentlyUseCase {

    @Inject
    FirebaseRecentlyRepository repository;

    @Inject
    SaveRecentlyUseCase() {
    }

    public Single<String> execute(String myUserId, String otherUserId) {
        return repository.saveRecently(myUserId, otherUserId);
    }
}
