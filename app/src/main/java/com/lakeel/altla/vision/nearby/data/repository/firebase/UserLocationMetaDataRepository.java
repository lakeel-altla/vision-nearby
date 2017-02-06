package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.LocationMetaDataEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.entity.LocationDataEntityMapper;
import com.lakeel.altla.vision.nearby.data.mapper.model.LocationMetaDataMapper;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public final class UserLocationMetaDataRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userLocationMetaData";

    private static final String KEY_BEACON_ID = "beaconId";

    private final LocationDataEntityMapper entityMapper = new LocationDataEntityMapper();

    private final LocationMetaDataMapper metaDataMapper = new LocationMetaDataMapper();

    private final DatabaseReference reference;

    @Inject
    UserLocationMetaDataRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Single<LocationMetaData> findLatest(String userId, String beaconId) {
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
                                    subscriber.onSuccess(metaDataMapper.map(snapshot));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable save(String uniqueId, String userId, String beaconId) {
        return Completable.create(subscriber -> {
            LocationMetaDataEntity entity = entityMapper.map(beaconId);
            Task task = reference
                    .child(userId)
                    .child(uniqueId)
                    .setValue(entity.toMap());

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(new DataStoreException(exception));
            }

            subscriber.onCompleted();
        });
    }
}
