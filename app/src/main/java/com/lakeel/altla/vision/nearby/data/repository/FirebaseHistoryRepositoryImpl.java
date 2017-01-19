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

import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

public class FirebaseHistoryRepositoryImpl implements FirebaseHistoryRepository {

    private static final String ID_KEY = "userId";

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
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    HistoryEntity entity = snapshot.getValue(HistoryEntity.class);
                                    entity.uniqueId = snapshot.getKey();
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
    public Single<Long> findTimes(String myUserId, String otherUserId) {
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
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(otherUserId);

            DatabaseReference pushedReference = reference.child(myUserId).push();
            String uniqueId = pushedReference.getKey();

            Task<Void> task = pushedReference
                    .setValue(entity.toMap());

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(uniqueId);
        });
    }

    @Override
    public Single<HistoryEntity> saveUserActivity(String uniqueId, String userId, DetectedActivity detectedActivity) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(detectedActivity);
            Map<String, Object> map = entity.toUserActivityMap();

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(entity);
        });
    }

    @Override
    public Single<HistoryEntity> saveCurrentLocation(String uniqueId, String userId, Location location) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(location);
            Map<String, Object> map = entity.toLocationMap();

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(entity);
        });
    }

    @Override
    public Single<HistoryEntity> saveWeather(String uniqueId, String userId, Weather weather) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(weather);
            Map<String, Object> map = entity.toWeatherMap();

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(entity);
        });
    }

    @Override
    public Completable removeByUniqueKey(String userId, String uniqueKey) {
        return Completable.create(subscriber -> {
            Task task = reference.
                    child(userId)
                    .child(uniqueKey)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}
