package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindTimesUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    public FindTimesUseCase() {
    }

    public Single<Long> execute(String id) {
        return mFirebaseRecentlyRepository.findTimes(id);
    }
}
