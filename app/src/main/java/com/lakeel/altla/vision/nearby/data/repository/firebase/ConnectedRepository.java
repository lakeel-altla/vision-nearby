package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import rx.Completable;

public final class ConnectedRepository {

    private DatabaseReference reference;

    @Inject
    public ConnectedRepository(String url) {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Completable observe() {
        return Completable.create(subscriber ->
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean connected = dataSnapshot.getValue(Boolean.class);
                        if (connected) {
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }
}