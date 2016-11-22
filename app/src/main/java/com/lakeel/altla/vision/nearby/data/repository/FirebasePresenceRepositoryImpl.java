package com.lakeel.altla.vision.nearby.data.repository;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.data.entity.PresencesEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresenceRepository;

import javax.inject.Inject;

import rx.Single;

public class FirebasePresenceRepositoryImpl implements FirebasePresenceRepository {

    private static final String IS_CONNECTED_KEY = "isConnected";

    private static final String LAST_ONLINE_KEY = "lastOnline";

    private DatabaseReference mReference;

    @Inject
    public FirebasePresenceRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public void savePresenceOnline() {
        // Check user authentication because when user sign out, this method is called and FirebaseUser instance become null.
        if (MyUser.isAuthenticated()) {
            mReference.child(MyUser.getUid()).child(IS_CONNECTED_KEY).setValue(true);
            mReference.child(MyUser.getUid()).child(LAST_ONLINE_KEY).setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    public void savePresenceOfflineOnDisconnected() {
        mReference
                .child(MyUser.getUid())
                .child(IS_CONNECTED_KEY)
                .onDisconnect()
                .setValue(false);
    }

    @Override
    public Single<PresencesEntity> findPresenceById(String id) {
        return Single.create(subscriber ->
                mReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PresencesEntity entity = dataSnapshot.getValue(PresencesEntity.class);
                        subscriber.onSuccess(entity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }


}
