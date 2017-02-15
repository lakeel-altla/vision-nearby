package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public class BeaconRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/beacons";

    private static final String KEY_IS_LOST = "isLost";

    private static final String KEY_LAST_USED_TIME = "lastUsedTime";

    private final DatabaseReference reference;

    @Inject
    public BeaconRepository() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Single<Beacon> find(String beaconId) {
        return Single.create(subscriber ->
                reference
                        .child(beaconId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<String> save(Beacon beacon) {
        return Single.create(subscriber -> {

            Task task = reference
                    .child(beacon.beaconId)
                    .setValue(beacon);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onSuccess(beacon.beaconId);
        });
    }

    public Single<String> remove(String beaconId) {
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

    public Completable saveLastUsedDeviceTime(String beaconId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_LAST_USED_TIME, ServerValue.TIMESTAMP);

            Task task = reference
                    .child(beaconId)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onCompleted();
        });
    }

    @Nullable
    private Beacon map(DataSnapshot dataSnapshot) {
        Beacon beacon = dataSnapshot.getValue(Beacon.class);
        if (beacon == null) {
            return null;
        }
        beacon.beaconId = dataSnapshot.getKey();
        return beacon;
    }
}