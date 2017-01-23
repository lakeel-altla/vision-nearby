package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.domain.entity.BeaconEntity;

import rx.Completable;
import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveBeacon(String beaconId, String userId, String name);

    Single<BeaconEntity> findBeacon(String beaconId);

    Single<String> removeBeacon(String beaconId);

    Completable lostDevice(String beaconId);

    Completable foundDevice(String beaconId);
}
