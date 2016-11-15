package com.lakeel.profile.notification.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.data.entity.BeaconsEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.data.mapper.BeaconsEntityMapper;
import com.lakeel.profile.notification.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class FirebaseBeaconsRepositoryImpl implements FirebaseBeaconsRepository {

    private DatabaseReference mReference;

    private BeaconsEntityMapper mMapper = new BeaconsEntityMapper();

    @Inject
    public FirebaseBeaconsRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveBeacon(String beaconId, String name) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                BeaconsEntity entity = mMapper.map(name);

                Task task = mReference
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
    public Single<BeaconsEntity> findBeaconById(String beaconId) {
        return Single.create(new Single.OnSubscribe<BeaconsEntity>() {
            @Override
            public void call(SingleSubscriber<? super BeaconsEntity> subscriber) {
                mReference
                        .child(beaconId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                BeaconsEntity entity = snapshot.getValue(BeaconsEntity.class);
                                entity.key = snapshot.getKey();
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }
}
