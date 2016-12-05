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
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.HistoryEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

public class FirebaseHistoryRepositoryImpl implements FirebaseHistoryRepository {

    private static final String ID_KEY = "userId";

    private static final String TIMESTAMP_KEY = "passingTime";

    private static final String LOCATION_KEY = "location";

    private static final String TEXT_KEY = "text";

    private DatabaseReference reference;

    private HistoryEntityMapper entityMapper = new HistoryEntityMapper();
    
    @Inject
    public FirebaseHistoryRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Observable<HistoryEntity> findHistoryByUserId(String userId) {
        return Observable.create(new Observable.OnSubscribe<HistoryEntity>() {

            @Override
            public void call(Subscriber<? super HistoryEntity> subscriber) {
                reference
                        .child(userId)
                        .orderByChild(TIMESTAMP_KEY)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    HistoryEntity entity = snapshot.getValue(HistoryEntity.class);
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
    public Single<HistoryEntity> findHistoryByUserIdAndUniqueKey(String userId, String uniqueKey) {
        return Single.create(new Single.OnSubscribe<HistoryEntity>() {
            @Override
            public void call(SingleSubscriber<? super HistoryEntity> subscriber) {
                reference
                        .child(userId)
                        .child(uniqueKey)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HistoryEntity entity = dataSnapshot.getValue(HistoryEntity.class);
                                subscriber.onSuccess(entity);
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
                reference
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
    public Single<String> saveHistory(String myUserId, String otherUserId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                HistoryEntity entity = entityMapper.map(otherUserId);

                DatabaseReference pushedReference = reference.child(myUserId).push();
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
    public Single<HistoryEntity> saveDetectedActivity(String uniqueId, String userId, DetectedActivity detectedActivity) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(detectedActivity);

            Task<Void> task = reference
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
    public Single<HistoryEntity> saveCurrentLocation(String uniqueId, String userId, Location location) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(location);

            Task<Void> task = reference
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
    public Single<HistoryEntity> saveWeather(String uniqueId, String userId, Weather weather) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(weather);

            Task<Void> task = reference
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

            Task task = reference
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
