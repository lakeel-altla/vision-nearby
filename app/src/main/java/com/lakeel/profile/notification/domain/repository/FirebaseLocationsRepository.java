package com.lakeel.profile.notification.domain.repository;

import com.firebase.geofire.GeoLocation;

import rx.Single;

public interface FirebaseLocationsRepository {

    Single<GeoLocation> findLocationByKey(String key);
}
