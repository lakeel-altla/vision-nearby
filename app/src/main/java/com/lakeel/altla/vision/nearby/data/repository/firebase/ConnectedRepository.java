package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import rx.Completable;

public final class ConnectedRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/.info/connected";

    private DatabaseReference reference;

    @Inject
    ConnectedRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
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