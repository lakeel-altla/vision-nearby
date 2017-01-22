package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveHistoryUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    RemoveHistoryUseCase() {
    }

    public Completable execute(String uniqueKey) {
        String userId = MyUser.getUserId();
        return repository.removeByUniqueKey(userId, uniqueKey).subscribeOn(Schedulers.io());
    }
}
