package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    RemoveNearbyHistoryUseCase() {
    }

    public Completable execute(String uniqueKey) {
        String userId = MyUser.getUserId();
        return repository.remove(userId, uniqueKey).subscribeOn(Schedulers.io());
    }
}
