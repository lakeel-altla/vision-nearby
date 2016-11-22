package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;

import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveBeacon(String beaconId, String name);

    Single<BeaconsEntity> findBeaconById(String beaconId);
}
