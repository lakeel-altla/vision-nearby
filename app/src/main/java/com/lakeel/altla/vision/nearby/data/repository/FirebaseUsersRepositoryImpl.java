package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.UserEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

public final class FirebaseUsersRepositoryImpl implements FirebaseUsersRepository {

    private static final String KEY_BEACONS = "beacons";

    private DatabaseReference reference;

    private UserEntityMapper entityMapper = new UserEntityMapper();

    @Inject
    public FirebaseUsersRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Completable saveUser(String userId) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = entityMapper.map();
            Task task = reference
                    .child(userId)
                    .updateChildren(map)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }
        });
    }

    @Override
    public Single<String> saveBeacon(String userId, String beaconId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                Map<String, Object> map = new HashMap<>();
                map.put(beaconId, true);

                reference
                        .child(userId)
                        .child(KEY_BEACONS)
                        .updateChildren(map)
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(beaconId))
                        .addOnFailureListener(subscriber::onError);
            }
        });
    }

    @Override
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

    @Override
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
                                entity.key = dataSnapshot.getKey();
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
