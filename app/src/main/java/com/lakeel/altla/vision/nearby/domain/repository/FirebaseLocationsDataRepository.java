package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.domain.entity.LocationDataEntity;

import rx.Single;

public interface FirebaseLocationsDataRepository {

    Single<LocationDataEntity> findLocationsDataByBeaconId(String beaconId);

    Single<LocationDataEntity> saveLocationData(String uniqueId, String beaconId);
}
