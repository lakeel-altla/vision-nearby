package com.lakeel.altla.vision.nearby.domain.repository;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseRecentlyRepository {

    Observable<RecentlyEntity> findRecentlyByUserId(String userId);

    Single<Long> findPassingTimes(String userId, String otherId);

    Single<String> saveRecently(String userId);

    Single<RecentlyEntity> saveDetectedActivity(String uniqueId, String userId, DetectedActivity detectedActivity);

    Single<RecentlyEntity> saveCurrentLocation(String uniqueId, String userId, Location location);

    Single<RecentlyEntity> saveWeather(String uniqueId, String userId, Weather weather);

    Single<String> saveLocationText(String key, String userId, String language, String locationText);
}
