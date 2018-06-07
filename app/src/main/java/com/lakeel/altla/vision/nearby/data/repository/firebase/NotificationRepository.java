package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.domain.model.Notification;

import javax.inject.Inject;

import rx.Completable;

public class NotificationRepository {

    private final DatabaseReference reference;

    @Inject
    public NotificationRepository(String url) {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Completable save(@NonNull Notification notification) {
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