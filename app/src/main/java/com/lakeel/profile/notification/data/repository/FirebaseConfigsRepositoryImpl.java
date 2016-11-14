package com.lakeel.profile.notification.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.profile.notification.data.entity.ConfigsEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseConfigsRepository;

import javax.inject.Inject;

import rx.Single;

public class FirebaseConfigsRepositoryImpl implements FirebaseConfigsRepository {

    private DatabaseReference mReference;

    @Inject
    public FirebaseConfigsRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<ConfigsEntity> find() {
        return Single.create(subscriber -> mReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
