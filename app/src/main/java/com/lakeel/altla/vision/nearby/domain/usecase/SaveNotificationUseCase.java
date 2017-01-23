package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseNotificationsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveNotificationUseCase {

    @Inject
    FirebaseNotificationsRepository repository;

    @Inject
    SaveNotificationUseCase() {
    }

    public Single<NotificationEntity> execute(String to, String title, String message) {
        return repository.saveNotification(to, title, message).subscribeOn(Schedulers.io());
    }
}
