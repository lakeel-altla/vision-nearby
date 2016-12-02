package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;

import rx.Single;

public interface FirebaseNotificationsRepository {

    Single<NotificationEntity> saveNotification(String to, String title, String message);
}
