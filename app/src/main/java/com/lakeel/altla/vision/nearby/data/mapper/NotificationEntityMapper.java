package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.domain.entity.NotificationEntity;

public final class NotificationEntityMapper {

    public NotificationEntity map(String to, String title, String message) {
        NotificationEntity entity = new NotificationEntity();
        entity.to = to;
        entity.title = title;
        entity.message = message;
        return entity;
    }

}
