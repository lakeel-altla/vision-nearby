package com.lakeel.altla.vision.nearby.domain.repository;

import android.location.Location;

import com.firebase.geofire.GeoLocation;

import rx.Single;

public interface FirebaseLocationsRepository {

    Single<GeoLocation> findLocationByKey(String key);

    Single<String> saveLocation(Location location);
}
