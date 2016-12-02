package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.BeaconsEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveUserBeacon(String beaconId, String name);

    Observable<BeaconsEntity> findBeaconsByUserId(String userId);

    Single<BeaconsEntity> findBeacon(String beaconId);
}
