package com.lakeel.altla.vision.nearby.data.repository;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.RecentlyEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

public class FirebaseRecentlyRepositoryImpl implements FirebaseRecentlyRepository {

    private static final String ID_KEY = "userId";

    private static final String TIMESTAMP_KEY = "passingTime";

    private static final String LOCATION_KEY = "location";

    private static final String TEXT_KEY = "text";

    private DatabaseReference databaseReference;

    private RecentlyEntityMapper entityMapper = new RecentlyEntityMapper();

    @Inject
    FirebaseUsersRepository mFirebaseUsersRepository;

    @Inject
    public FirebaseRecentlyRepositoryImpl(String url) {
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Observable<RecentlyEntity> findRecentlyByUserId(String userId) {
        return Observable.create(new Observable.OnSubscribe<RecentlyEntity>() {

            @Override
            public void call(Subscriber<? super RecentlyEntity> subscriber) {
                databaseReference
                        .child(userId)
                        .orderByChild(TIMESTAMP_KEY)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    RecentlyEntity entity = snapshot.getValue(RecentlyEntity.class);
                                    entity.key = snapshot.getKey();
                                    subscriber.onNext(entity);
                                }
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        });
            }
        });
    }

    @Override
    public Single<Long> findPassingTimes(String myUserId, String otherUserId) {
        return Single.create(subscriber ->
                databaseReference
                        .child(myUserId)
                        .orderByChild(ID_KEY)
                        .equalTo(otherUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(dataSnapshot.getChildrenCount());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<String> saveRecently(String userId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                RecentlyEntity entity = entityMapper.map(userId);

                DatabaseReference pushedReference = databaseReference.child(userId).push();
                String uniqueId = pushedReference.getKey();

                Task<Void> task = pushedReference
                        .setValue(entity.toMap())
                        .addOnSuccessListener(aVoid -> subscriber.onSuccess(uniqueId))
                        .addOnFailureListener(subscriber::onError);

                Exception exception = task.getException();
                if (exception != null) {
                    throw new DataStoreException(exception);
                }
            }
        });
    }

    @Override
    public Single<RecentlyEntity> saveDetectedActivity(String uniqueId, String userId, DetectedActivity detectedActivity) {
        return Single.create(subscriber -> {
            RecentlyEntity entity = entityMapper.map(detectedActivity);

            Task<Void> task = databaseReference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(entity.toUserActivityMap())
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Single<RecentlyEntity> saveCurrentLocation(String uniqueId, String userId, Location location) {
        return Single.create(subscriber -> {
            RecentlyEntity entity = entityMapper.map(location);

            Task<Void> task = databaseReference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(entity.toLocationMap())
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Single<RecentlyEntity> saveWeather(String uniqueId, String userId, Weather weather) {
        return Single.create(subscriber -> {
            RecentlyEntity entity = entityMapper.map(weather);

            Task<Void> task = databaseReference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(entity.toWeatherMap())
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Single<String> saveLocationText(String key, String userId, String language, String locationText) {
        return Single.create(subscriber -> {
            HashMap<String, Object> value = new HashMap<>();
            value.put(language, locationText);

            Task task = databaseReference
                    .child(userId)
                    .child(key)
                    .child(LOCATION_KEY)
                    .child(TEXT_KEY)
                    .updateChildren(value)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(locationText))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }
}
