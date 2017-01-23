package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindTimesUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    FindTimesUseCase() {
    }

    public Single<Long> execute(String passingUserId) {
        String userId = MyUser.getUserId();
        return repository.findTimes(userId, passingUserId).subscribeOn(Schedulers.io());
    }
}
