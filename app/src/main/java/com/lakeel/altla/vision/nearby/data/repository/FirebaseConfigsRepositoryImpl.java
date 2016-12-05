package com.lakeel.altla.vision.nearby.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.data.entity.ConfigsEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConfigsRepository;

import javax.inject.Inject;

import rx.Single;

public class FirebaseConfigsRepositoryImpl implements FirebaseConfigsRepository {

    private DatabaseReference reference;

    @Inject
    public FirebaseConfigsRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<ConfigsEntity> find() {
        return Single.create(subscriber -> reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConfigsEntity entity = dataSnapshot.getValue(ConfigsEntity.class);
                subscriber.onSuccess(entity);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                subscriber.onError(databaseError.toException());
            }
        }));
    }
}
