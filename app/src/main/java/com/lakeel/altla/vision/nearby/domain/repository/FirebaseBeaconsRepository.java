package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveUserBeacon(String beaconId, String userId, String name);

    Observable<BeaconEntity> findBeaconsByUserId(String userId);

    Single<BeaconEntity> findBeacon(String beaconId);
}
