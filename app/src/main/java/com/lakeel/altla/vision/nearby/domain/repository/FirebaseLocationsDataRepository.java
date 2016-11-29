package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.LocationsDataEntity;

import rx.Single;

public interface FirebaseLocationsDataRepository {

    Single<LocationsDataEntity> findLocationsDataById(String id);

    Single<LocationsDataEntity> saveLocationData(String uniqueId, String beaconId);
}
