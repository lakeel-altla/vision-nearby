package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.model.DeviceTokenMapper;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public final class UserDeviceTokenRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userDeviceTokens";

    private final DeviceTokenMapper deviceTokenMapper = new DeviceTokenMapper();

    private final DatabaseReference reference;

    @Inject
    UserDeviceTokenRepository() {
        this.reference = FirebaseDatabase.getInstance().getReference(DATABASE_URI);
    }

    public Observable<DeviceToken> findAll(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String to = dataSnapshot.getKey();
                                String beaconId = snapshot.getKey();
                                String token = (String) snapshot.getValue();
                                subscriber.onNext(deviceTokenMapper.map(to, beaconId, token));
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

    public Single<String> find(String userId) {
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

    public Single<String> save(String userId, String beaconId, String token) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(beaconId)
                    .setValue(token);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onSuccess(token);
        });
    }
}