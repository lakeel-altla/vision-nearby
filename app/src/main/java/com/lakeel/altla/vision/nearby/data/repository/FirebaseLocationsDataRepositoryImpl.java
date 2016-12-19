package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.LocationDataEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;

public final class FirebaseLocationsDataRepositoryImpl implements FirebaseLocationsDataRepository {

    private static final String KEY_BEACON_ID = "beaconId";

    private DatabaseReference reference;

    private final LocationDataEntityMapper entityMapper = new LocationDataEntityMapper();

    @Inject
    public FirebaseLocationsDataRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<LocationDataEntity> findLocationsDataByBeaconId(String beaconId) {
        return Single.create(subscriber ->
                reference
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

                                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                                Iterator<DataSnapshot> iterator = iterable.iterator();
                                while (iterator.hasNext()) {
                                    DataSnapshot snapshot = iterator.next();
                                    LocationDataEntity entity = snapshot.getValue(LocationDataEntity.class);
                                    entity.uniqueId = snapshot.getKey();
                                    subscriber.onSuccess(entity);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<LocationDataEntity> saveLocationData(String uniqueId, String beaconId) {
        return Single.create(subscriber -> {
            LocationDataEntity entity = entityMapper.map(beaconId);
            Task task = reference
                    .child(uniqueId)
                    .setValue(entity.toMap());

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(new DataStoreException(exception));
            }

            subscriber.onSuccess(entity);
        });
    }
}
