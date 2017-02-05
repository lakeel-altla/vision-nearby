package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindAllPassingTimeUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    FindAllPassingTimeUseCase() {
    }

    public Single<Long> execute(String passingUserId) {
        String userId = MyUser.getUserId();
        return repository.findPassingTimes(userId, passingUserId).subscribeOn(Schedulers.io());
    }
}
