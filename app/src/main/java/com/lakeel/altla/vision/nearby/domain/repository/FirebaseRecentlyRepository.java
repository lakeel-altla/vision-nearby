package com.lakeel.altla.vision.nearby.domain.repository;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseRecentlyRepository {

    Observable<RecentlyEntity> findRecently();

    Single<Long> findTimes(String id);

    Single<String> saveRecently(String userId);

    Single<RecentlyEntity> saveDetectedActivity(String uniqueId, DetectedActivity detectedActivity);

    Single<RecentlyEntity> saveCurrentLocation(String uniqueId, Location location);

    Single<RecentlyEntity> saveWeather(String uniqueId, Weather weather);

    Single<String> saveLocationText(String key, String language, String locationText);
}
