package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseNotificationsRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveNotificationUseCase {

    @Inject
    FirebaseNotificationsRepository repository;

    @Inject
    SaveNotificationUseCase() {
    }

    public Completable execute(String to, String title, String message) {
        return repository.save(to, title, message).subscribeOn(Schedulers.io());
    }
}
