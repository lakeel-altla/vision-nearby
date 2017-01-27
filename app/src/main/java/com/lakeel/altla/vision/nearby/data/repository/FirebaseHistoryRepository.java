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
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.HistoryEntityMapper;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Completable;
import rx.Observable;
import rx.Single;
import rx.Subscriber;

public class FirebaseHistoryRepository {

    private static final String ID_KEY = "userId";

    private DatabaseReference reference;

    private HistoryEntityMapper entityMapper = new HistoryEntityMapper();

    @Inject
    public FirebaseHistoryRepository(@Named("historyUrl") String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    public Observable<HistoryEntity> findHistoryList(String userId) {
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

    public Single<HistoryEntity> findHistory(String userId, String historyId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(historyId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                HistoryEntity entity = snapshot.getValue(HistoryEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<Long> findTimes(String myUserId, String passingUserId) {
        return Single.create(subscriber ->
                reference
                        .child(myUserId)
                        .orderByChild(ID_KEY)
                        .equalTo(passingUserId)
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

    public Single<String> saveHistory(String myUserId, String passingUserId, String regionState) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(passingUserId, regionState);

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

    public Single<HistoryEntity> saveUserActivity(String uniqueId, String userId, DetectedActivity userActivity) {
        return Single.create(subscriber -> {
            HistoryEntity entity = entityMapper.map(userActivity);
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
