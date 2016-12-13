package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;

import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveBeacon(String beaconId, String userId, String name);

    Single<BeaconEntity> findBeacon(String beaconId);

    Single<String> removeBeaconByBeaconId(String beaconId);
}
