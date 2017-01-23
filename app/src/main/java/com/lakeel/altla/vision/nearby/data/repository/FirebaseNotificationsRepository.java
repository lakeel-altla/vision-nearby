package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.data.mapper.NotificationEntityMapper;
import com.lakeel.altla.vision.nearby.domain.entity.NotificationEntity;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Single;

public class FirebaseNotificationsRepository {

    private DatabaseReference reference;

    private NotificationEntityMapper entityMapper = new NotificationEntityMapper();

    @Inject
    public FirebaseNotificationsRepository(@Named("notificationsUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Single<NotificationEntity> saveNotification(String to, String title, String message) {
        return Single.create(subscriber -> {
            NotificationEntity entity = entityMapper.map(to, title, message);
            Map<String, Object> map = entity.toMap();

            Task task = reference
                    .push()
                    .setValue(map);

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(exception);
            }

            subscriber.onSuccess(entity);
        });
    }
}
