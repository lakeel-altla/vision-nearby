package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public final class UserProfileRepository {

    private static final String KEY_BEACONS = "beacons";

    private final DatabaseReference reference;

    @Inject
    public UserProfileRepository(String url) {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Single<UserProfile> find(@NonNull String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(userId, dataSnapshot));
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Observable<String> findUserBeacons(@NonNull String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Return beacon id.
                                subscriber.onNext(snapshot.getKey());
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

    public Observable<UserProfile> save(UserProfile userProfile) {
        return Observable.create(subscriber -> {
            Task task = reference
                    .child(userProfile.userId)
                    .setValue(userProfile);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onNext(userProfile);
        });
    }

    public Observable<String> saveUserBeacon(@NonNull String userId, @NonNull String beaconId) {
        return Observable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(beaconId, true);

            Task task = reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onNext(beaconId);
        });
    }

    public Single<String> removeUserBeacon(@NonNull String userId, @NonNull String beaconId) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .child(beaconId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onSuccess(beaconId);
        });
    }

    public Observable<UserProfile> observeProfile(String userId) {
        return Observable.create(subscriber ->
                reference
                        .child(userId)
                        .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onNext(map(userId, dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    private UserProfile map(String userId, DataSnapshot dataSnapshot) {
        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
        userProfile.userId = userId;
        return userProfile;
    }
}