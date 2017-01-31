package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.entity.BeaconEntityMapper;
import com.lakeel.altla.vision.nearby.data.mapper.model.BeaconMapper;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Single;

public class FirebaseBeaconRepository {

    private static final String KEY_IS_LOST = "isLost";

    private final BeaconEntityMapper entityMapper = new BeaconEntityMapper();

    private final BeaconMapper beaconMapper = new BeaconMapper();

    private final DatabaseReference reference;

    @Inject
    public FirebaseBeaconRepository(@Named("beaconUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Single<String> saveBeacon(String beaconId, String userId, String name) {
        return Single.create(subscriber -> {
            BeaconEntity entity = entityMapper.map(userId, name);
            Map<String, Object> map = entity.toMap();

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onSuccess(beaconId);
        });
    }

    public Single<Beacon> findBeacon(String beaconId) {
        return Single.create(subscriber ->
                reference
                        .child(beaconId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BeaconEntity entity = dataSnapshot.getValue(BeaconEntity.class);
                                if (entity == null) {
                                    subscriber.onSuccess(null);
                                } else {
                                    subscriber.onSuccess(beaconMapper.map(entity, dataSnapshot.getKey()));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<String> removeBeacon(String beaconId) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(beaconId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onSuccess(beaconId);
        });
    }

    public Completable lostDevice(String beaconId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_IS_LOST, true);

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    public Completable foundDevice(String beaconId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_IS_LOST, false);

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}