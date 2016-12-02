package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.BeaconsEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;

public class FirebaseBeaconsRepositoryImpl implements FirebaseBeaconsRepository {

    private static final String KEY_NAME = "name";

    private DatabaseReference reference;

    private BeaconsEntityMapper mMapper = new BeaconsEntityMapper();

    @Inject
    public FirebaseBeaconsRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveUserBeacon(String beaconId, String name) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                BeaconsEntity entity = mMapper.map(name);

                Task task = reference
                        .child(beaconId)
                        .setValue(entity)
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
    public Observable<BeaconsEntity> findBeaconsByUserId(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .orderByChild(KEY_NAME)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                BeaconsEntity entity = snapshot.getValue(BeaconsEntity.class);
                                entity.key = snapshot.getKey();
                                subscriber.onNext(entity);
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

    @Override
    public Single<BeaconsEntity> findBeacon(String beaconId) {
        return Single.create(subscriber ->
                reference
                        .child(beaconId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                BeaconsEntity entity = dataSnapshot.getValue(BeaconsEntity.class);
                                entity.key = dataSnapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }
}
