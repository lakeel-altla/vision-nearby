package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindPassingTimesUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository repository;

    @Inject
    FindPassingTimesUseCase() {
    }

    public Single<Long> execute(String passingUserId) {
        String userId = MyUser.getUserId();
        return repository.findPassingTimes(userId, passingUserId).subscribeOn(Schedulers.io());
    }
}
