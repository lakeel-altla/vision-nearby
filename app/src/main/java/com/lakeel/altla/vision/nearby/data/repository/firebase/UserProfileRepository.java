package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public final class UserProfileRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userProfiles";

    private static final String KEY_BEACONS = "beacons";

    private final DatabaseReference reference;

    @Inject
    public UserProfileRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Single<UserProfile> find(String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(userId, dataSnapshot));
                            }

                            @Override
                            public void onCancelled(final DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable save(String userId) {
        return Completable.create(subscriber -> {
            UserProfile userProfile = new UserProfile();
            FirebaseUser firebaseUser = CurrentUser.getUser();
            userProfile.name = firebaseUser.getDisplayName();
            userProfile.email = firebaseUser.getEmail();
            if (firebaseUser.getPhotoUrl() != null) {
                userProfile.imageUri = firebaseUser.getPhotoUrl().toString();
            }

            Task task = reference
                    .child(userId)
                    .setValue(userProfile);

            Exception e = task.getException();
            if (e != null) {
                throw new DataStoreException(e);
            }

            subscriber.onCompleted();
        });
    }

    public Observable<String> findUserBeacons(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String beaconId = snapshot.getKey();
                                subscriber.onNext(beaconId);
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

    public Single<String> saveUserBeacon(String userId, String beaconId) {
        return Single.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(beaconId, true);

            Task task = reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .updateChildren(map);

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onSuccess(beaconId);
        });
    }

    public Single<String> removeUserBeacon(String userId, String beaconId) {
        return Single.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(KEY_BEACONS)
                    .child(beaconId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onSuccess(beaconId);
        });
    }

    public Observable<UserProfile> observeProfile(String userId) {
        return Observable.create(subscriber ->
                reference
                        .child(userId)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onNext(map(userId, dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    private UserProfile map(String userId, DataSnapshot dataSnapshot) {
        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
        userProfile.userId = userId;
        return userProfile;
    }
}