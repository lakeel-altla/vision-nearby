package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class OfflineUseCase {

    @Inject
    UserConnectionRepository repository;

    @Inject
    OfflineUseCase() {
    }

    public Completable execute() {
        String userId = CurrentUser.getUid();
        return repository.saveOffline(userId).subscribeOn(Schedulers.io());
    }
}
