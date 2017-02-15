package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.domain.model.Connection;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public class UserConnectionRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userConnections";

    private static final String IS_CONNECTED_KEY = "isConnected";

    private final DatabaseReference reference;

    @Inject
    UserConnectionRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public void saveOnline(String userId) {

        // Check user authentication because when user sign out,
        // this method is called and FirebaseUser instance become null.

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Connection connection = new Connection();
            connection.isConnected = true;
            connection.lastOnlineTime = ServerValue.TIMESTAMP;

            reference
                    .child(userId)
                    .setValue(connection);
        }
    }

    public Completable saveOffline(String userId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(IS_CONNECTED_KEY)
                    .setValue(false);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    public void saveOfflineOnDisconnect(String userId) {
        reference
                .child(userId)
                .child(IS_CONNECTED_KEY)
                .onDisconnect()
                .setValue(false);
    }

    public Single<Connection> find(String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    private Connection map(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(Connection.class);
    }
}