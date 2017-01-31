package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveNearbyHistoryUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository repository;

    @Inject
    RemoveNearbyHistoryUseCase() {
    }

    public Completable execute(String uniqueKey) {
        String userId = MyUser.getUserId();
        return repository.removeNearbyHistory(userId, uniqueKey).subscribeOn(Schedulers.io());
    }
}
