package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.BeaconEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;
import rx.SingleSubscriber;

public class FirebaseBeaconsRepositoryImpl implements FirebaseBeaconsRepository {

    private static final String KEY_IS_LOST = "isLost";

    private DatabaseReference reference;

    private BeaconEntityMapper entityMapper = new BeaconEntityMapper();

    @Inject
    public FirebaseBeaconsRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveBeacon(String beaconId, String userId, String name) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                BeaconEntity entity = entityMapper.map(userId, name);
                Map<String, Object> map = entity.toMap();

                Task task = reference
                        .child(beaconId)
                        .updateChildren(map)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(beaconId))
                        .addOnFailureListener(subscriber::onError);

                Exception e = task.getException();
                if (e != null) {
                    throw new DataStoreException(e);
                }
            }
        });
    }

    @Override
    public Single<BeaconEntity> findBeacon(String beaconId) {
        return Single.create(subscriber ->
                reference
                        .child(beaconId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BeaconEntity entity = dataSnapshot.getValue(BeaconEntity.class);
                                entity.key = dataSnapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<String> removeBeaconByBeaconId(String beaconId) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(beaconId)
                    .removeValue()
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(beaconId))
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Completable lostDevice(String beaconId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_IS_LOST, true);

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Completable foundDevice(String beaconId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_IS_LOST, false);

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }
        });
    }
}
