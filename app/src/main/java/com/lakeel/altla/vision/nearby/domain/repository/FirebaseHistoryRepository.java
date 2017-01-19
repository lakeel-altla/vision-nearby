package com.lakeel.altla.vision.nearby.domain.repository;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseHistoryRepository {

    Observable<HistoryEntity> findHistoryByUserId(String userId);

    Single<Long> findTimes(String userId, String otherId);

    Single<String> saveHistory(String myUserId, String otherUserId);

    Single<HistoryEntity> saveUserActivity(String uniqueId, String userId, DetectedActivity userActivity);

    Single<HistoryEntity> saveCurrentLocation(String uniqueId, String userId, Location location);

    Single<HistoryEntity> saveWeather(String uniqueId, String userId, Weather weather);

    Completable removeByUniqueKey(String userId, String uniqueKey);
}
