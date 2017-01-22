package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.domain.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.NotificationEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseNotificationsRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Single;

public class FirebaseNotificationsRepositoryImpl implements FirebaseNotificationsRepository {

    private DatabaseReference reference;

    private NotificationEntityMapper entityMapper = new NotificationEntityMapper();

    @Inject
    public FirebaseNotificationsRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
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
