package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.LocationMeta;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public final class UserLocationMetaDataRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userLocationMetaData";

    private static final String KEY_BEACON_ID = "beaconId";

    private final DatabaseReference reference;

    @Inject
    UserLocationMetaDataRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Single<LocationMeta> findLatest(String userId, String beaconId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .orderByChild(KEY_BEACON_ID)
                        .equalTo(beaconId)
                        .limitToFirst(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.hasChildren()) {
                                    subscriber.onSuccess(null);
                                    return;
                                }

                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()) {
                                    DataSnapshot snapshot = iterator.next();
                                    subscriber.onSuccess(map(userId, snapshot));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable save(LocationMeta locationMeta) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(locationMeta.userId)
                    .child(locationMeta.locationMetaDataId)
                    .setValue(locationMeta);

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(new DataStoreException(exception));
            }

            subscriber.onCompleted();
        });
    }

    private LocationMeta map(String userId, DataSnapshot dataSnapshot) {
        LocationMeta locationMeta = dataSnapshot.getValue(LocationMeta.class);
        locationMeta.userId = userId;
        locationMeta.locationMetaDataId = dataSnapshot.getKey();
        return locationMeta;
    }
}