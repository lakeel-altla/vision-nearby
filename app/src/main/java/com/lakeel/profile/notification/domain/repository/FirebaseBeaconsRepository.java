package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.BeaconsEntity;

import rx.Single;

public interface FirebaseBeaconsRepository {

    Single<String> saveBeacon(String beaconId, String name);

    Single<BeaconsEntity> findBeaconById(String beaconId);
}
