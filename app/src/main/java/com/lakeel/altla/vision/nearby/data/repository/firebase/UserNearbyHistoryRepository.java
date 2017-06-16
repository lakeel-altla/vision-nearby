package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public final class UserNearbyHistoryRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userNearbyHistory";

    private static final String KEY_USER_ID = "userId";

    private static final String KEY_IS_ENTERED = "isEntered";

    private static final String KEY_USER_ACTIVITY = "userActivity";

    private static final String KEY_LOCATION = "location";

    private static final String KEY_WEATHER = "weather";

    private final DatabaseReference reference;

    @Inject
    public UserNearbyHistoryRepository() {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Observable<NearbyHistory> findAll(@NonNull String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .orderByChild(KEY_IS_ENTERED)
                    .equalTo(true)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                subscriber.onNext(map(snapshot));
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

    public Single<NearbyHistory> findLatest(@NonNull String userId, @NonNull String favoriteUserId) {
        return Single.create(subscriber ->
                reference.child(userId)
                        .orderByChild(KEY_USER_ID)
                        .equalTo(favoriteUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot latestSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    NearbyHistory nearbyHistory = snapshot.getValue(NearbyHistory.class);

                                    if (!nearbyHistory.isEntered) {
                                        // only enter data.
                                        continue;
                                    }

                                    if (latestSnapshot == null) {
                                        latestSnapshot = snapshot;
                                        continue;
                                    }

                                    NearbyHistory latestNearbyHistory = latestSnapshot.getValue(NearbyHistory.class);
                                    if ((Long) nearbyHistory.passingTime > (Long) latestNearbyHistory.passingTime) {
                                        // Compare passing time.
                                        latestSnapshot = snapshot;
                                    }
                                }

                                subscriber.onSuccess(map(latestSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<NearbyHistory> find(@NonNull String userId, @NonNull String historyId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(historyId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                subscriber.onSuccess(map(snapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }


    public Single<Long> findPassingTimes(@NonNull String userId, @NonNull String passingUserId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .orderByChild(KEY_USER_ID)
                        .equalTo(passingUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long time = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    NearbyHistory entity = snapshot.getValue(NearbyHistory.class);
                                    if (entity.isEntered) {
                                        time++;
                                    }
                                }
                                subscriber.onSuccess(time);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<String> save(@NonNull String userId, @NonNull NearbyHistory nearbyHistory) {
        return Single.create(subscriber -> {
            DatabaseReference pushedReference = reference.child(userId).push();

            Task<Void> task = pushedReference
                    .setValue(nearbyHistory);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            // Return unique key.
            subscriber.onSuccess(pushedReference.getKey());
        });
    }

    public Completable saveUserActivity(@NonNull String userId, @NonNull String historyId, @NonNull DetectedActivity userActivity) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_USER_ACTIVITY, userActivity.getType());

            Task<Void> task = reference
                    .child(userId)
                    .child(historyId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable saveLocation(@NonNull String userId, @NonNull String historyId, @NonNull Location location) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_LOCATION, location);

            Task<Void> task = reference
                    .child(userId)
                    .child(historyId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable saveWeather(@NonNull String userId, @NonNull String historyId, @NonNull Weather weather) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_WEATHER, weather);

            Task<Void> task = reference
                    .child(userId)
                    .child(historyId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable remove(@NonNull String userId, @NonNull String historyId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(userId)
                    .child(historyId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }

    private NearbyHistory map(DataSnapshot dataSnapshot) {
        NearbyHistory nearbyHistory = dataSnapshot.getValue(NearbyHistory.class);
        nearbyHistory.historyId = dataSnapshot.getKey();
        return nearbyHistory;
    }
}