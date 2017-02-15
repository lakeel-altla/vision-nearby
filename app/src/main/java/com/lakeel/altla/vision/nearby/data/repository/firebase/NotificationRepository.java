package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.domain.model.Notification;

import javax.inject.Inject;

import rx.Completable;

public class NotificationRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/notifications";

    private final DatabaseReference reference;

    @Inject
    NotificationRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Completable save(Notification notification) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .push()
                    .setValue(notification);

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(exception);
            }

            subscriber.onCompleted();
        });
    }
}