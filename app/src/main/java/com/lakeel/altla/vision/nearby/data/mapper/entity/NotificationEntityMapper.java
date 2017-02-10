package com.lakeel.altla.vision.nearby.data.mapper.entity;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;

public final class NotificationEntityMapper {

    public NotificationEntity map(String to, String title, String message) {
        NotificationEntity entity = new NotificationEntity();
        entity.to = to;
        entity.title = title;
        entity.message = message;
        entity.registrationTime = ServerValue.TIMESTAMP;
        return entity;
    }
}
