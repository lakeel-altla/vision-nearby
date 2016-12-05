package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindTimesUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    public FindTimesUseCase() {
    }

    public Single<Long> execute(String myUserId, String otherUserId) {
        return repository.findPassingTimes(myUserId, otherUserId);
    }
}
