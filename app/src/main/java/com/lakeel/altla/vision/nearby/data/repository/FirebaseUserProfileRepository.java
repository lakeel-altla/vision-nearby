package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.entity.UserEntityMapper;
import com.lakeel.altla.vision.nearby.data.mapper.model.UserMapper;
import com.lakeel.altla.vision.nearby.domain.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Observable;
import rx.Single;

public final class FirebaseUserProfileRepository {

    private static final String KEY_BEACONS = "beacons";

    private final UserEntityMapper entityMapper = new UserEntityMapper();

    private final UserMapper userMapper = new UserMapper();

    private final DatabaseReference reference;

    @Inject
    public FirebaseUserProfileRepository(@Named("userProfileUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Single<User> findUser(String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                                subscriber.onSuccess(userMapper.map(entity, dataSnapshot.getKey()));
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable saveUser(String userId) {
        return Completable.create(subscriber -> {
            UserEntity entity = entityMapper.map();
            Task task = reference
                    .child(userId)
                    .updateChildren(entity.toMap());

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onCompleted();
        });
    }

    public Observable<String> findUserBeacons(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String beaconId = snapshot.getKey();
                                subscriber.onNext(beaconId);
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

    public Single<String> saveUserBeacon(String userId, String beaconId) {
        return Single.create(subscriber -> {
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

            subscriber.onSuccess(beaconId);
        });
    }

    public Single<String> removeUserBeacon(String userId, String beaconId) {
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

    public Observable<User> observeUserProfile(String userId) {
        return Observable.create(subscriber ->
                reference
                        .child(userId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                                subscriber.onNext(userMapper.map(entity, dataSnapshot.getKey()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
