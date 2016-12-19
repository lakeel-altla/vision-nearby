package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresencesRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public class FirebasePresencesRepositoryImpl implements FirebasePresencesRepository {

    private static final String IS_CONNECTED_KEY = "isConnected";

    private static final String LAST_ONLINE_KEY = "lastOnlineTime";

    private DatabaseReference reference;

    @Inject
    public FirebasePresencesRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public void savePresenceOnline(String userId) {
        // Check user authentication because when user sign out, this method is called and FirebaseUser instance become null.
        if (MyUser.isAuthenticated()) {
            Map<String, Object> map = new HashMap<>();
            map.put(IS_CONNECTED_KEY, true);
            map.put(LAST_ONLINE_KEY, ServerValue.TIMESTAMP);

            reference
                    .child(userId)
                    .updateChildren(map);
        }
    }

    @Override
    public Completable savePresenceOffline(String userId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(IS_CONNECTED_KEY)
                    .setValue(false)
                    .addOnSuccessListener(aVoid -> subscriber.onCompleted())
                    .addOnFailureListener(subscriber::onError);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public void savePresenceOfflineOnDisconnect(String userId) {
        reference
                .child(userId)
                .child(IS_CONNECTED_KEY)
                .onDisconnect()
                .setValue(false);
    }

    @Override
    public Single<PresenceEntity> findPresenceByUserId(String userId) {
        return Single.create(subscriber ->
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PresenceEntity entity = dataSnapshot.getValue(PresenceEntity.class);
                        subscriber.onSuccess(entity);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }


}
