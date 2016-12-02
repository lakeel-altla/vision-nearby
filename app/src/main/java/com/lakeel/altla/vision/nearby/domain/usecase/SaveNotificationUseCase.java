package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseNotificationsRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveNotificationUseCase {

    @Inject
    FirebaseNotificationsRepository repository;

    @Inject
    SaveNotificationUseCase() {
    }

    public Single<NotificationEntity> execute(String to, String title, String message) {
        return repository.saveNotification(to, title, message);
    }
}
