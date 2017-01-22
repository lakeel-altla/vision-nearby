package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionsRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class OfflineUseCase {

    @Inject
    FirebaseConnectionsRepository repository;

    @Inject
    OfflineUseCase() {
    }

    public Completable execute() {
        String userId = MyUser.getUserId();
        return repository.saveOffline(userId).subscribeOn(Schedulers.io());
    }
}
