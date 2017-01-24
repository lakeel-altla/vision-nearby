package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.UserEntityMapper;
import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

public final class FirebaseUsersRepository {

    private static final String KEY_BEACONS = "beacons";

    private DatabaseReference reference;

    private UserEntityMapper entityMapper = new UserEntityMapper();

    @Inject
    public FirebaseUsersRepository(@Named("usersUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Completable saveUser(String userId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = entityMapper.map();
            Task task = reference
                    .child(userId)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onCompleted();
        });
    }

    public Single<String> saveBeacon(String userId, String beaconId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
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
            }
        });
    }

    public Observable<String> findBeaconsByUserId(String userId) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
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
            }
        });
    }

    public Single<String> removeBeacon(String userId, String beaconId) {
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

    public Observable<UserEntity> observeUserProfile(String userId) {
        return Observable.create(subscriber ->
                reference
                        .child(userId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                                subscriber.onNext(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<UserEntity> findUserByUserId(String userId) {
        return Single.create(new Single.OnSubscribe<UserEntity>() {

            @Override
            public void call(SingleSubscriber<? super UserEntity> subscriber) {
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserEntity entity = dataSnapshot.getValue(UserEntity.class);
                                entity.userId = dataSnapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }
}