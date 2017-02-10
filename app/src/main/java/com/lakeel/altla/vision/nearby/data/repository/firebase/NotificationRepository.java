package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.entity.NotificationEntityMapper;

import javax.inject.Inject;

import rx.Completable;

public class NotificationRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/notifications";

    private final NotificationEntityMapper entityMapper = new NotificationEntityMapper();

    private final DatabaseReference reference;

    @Inject
    NotificationRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Completable save(String to, String title, String message) {
        return Completable.create(subscriber -> {
            NotificationEntity entity = entityMapper.map(to, title, message);
            Task task = reference
                    .push()
                    .setValue(entity);

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(exception);
            }

            subscriber.onCompleted();
        });
    }
}