package com.lakeel.altla.vision.nearby.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;

public final class FirebaseConnectionRepositoryImpl implements FirebaseConnectionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseConnectionRepositoryImpl.class);

    private DatabaseReference reference;

    @Inject
    public FirebaseConnectionRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Observable<Object> observeConnected() {
        return Observable.create(subscriber -> {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean connected = dataSnapshot.getValue(Boolean.class);
                    if (connected) {
                        subscriber.onNext(Observable.empty());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    LOGGER.error("Connection event was canceled", databaseError.toException());
                }
            });
        });
    }
}
