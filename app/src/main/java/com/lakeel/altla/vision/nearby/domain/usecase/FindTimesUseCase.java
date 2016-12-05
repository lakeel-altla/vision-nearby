package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindTimesUseCase {

    @Inject
    FirebaseHistoryRepository mFirebaseHistoryRepository;

    @Inject
    public FindTimesUseCase() {
    }

    public Single<Long> execute(String myUserId, String otherUserId) {
        return mFirebaseHistoryRepository.findPassingTimes(myUserId, otherUserId);
    }
}
