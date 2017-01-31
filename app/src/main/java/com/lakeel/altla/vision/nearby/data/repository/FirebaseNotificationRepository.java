package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.entity.NotificationEntityMapper;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;

public class FirebaseNotificationRepository {

    private final NotificationEntityMapper entityMapper = new NotificationEntityMapper();

    private final DatabaseReference reference;

    @Inject
    FirebaseNotificationRepository(@Named("notificationUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Completable saveNotification(String to, String title, String message) {
        return Completable.create(subscriber -> {
            NotificationEntity entity = entityMapper.map(to, title, message);
            Map<String, Object> map = entity.toMap();

            Task task = reference
                    .push()
                    .setValue(map);

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(exception);
            }

            subscriber.onCompleted();
        });
    }
}
