package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindTimesUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    public FindTimesUseCase() {
    }

    public Single<Long> execute(String myUserId, String otherUserId) {
        return mFirebaseRecentlyRepository.findPassingTimes(myUserId, otherUserId);
    }
}
