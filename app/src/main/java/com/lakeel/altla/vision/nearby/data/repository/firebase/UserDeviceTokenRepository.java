package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public final class UserDeviceTokenRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userDeviceTokens";

    private final DatabaseReference reference;

    @Inject
    UserDeviceTokenRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Observable<DeviceToken> findAll(@NonNull String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                subscriber.onNext(map(userId, snapshot));
                            }
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    public Single<String> find(@NonNull String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String token = (String) dataSnapshot.getValue();
                                subscriber.onSuccess(token);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Observable<DeviceToken> save(@NonNull DeviceToken deviceToken) {
        return Observable.create(subscriber -> {
            Task task = reference
                    .child(deviceToken.userId)
                    .child(deviceToken.beaconId)
                    .setValue(deviceToken.token);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onNext(deviceToken);
        });
    }

    private DeviceToken map(String userId, DataSnapshot dataSnapshot) {
        DeviceToken deviceToken = dataSnapshot.getValue(DeviceToken.class);
        deviceToken.userId = userId;
        deviceToken.beaconId = dataSnapshot.getKey();
        return deviceToken;
    }
}