package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseUsersRepository {

    Single<UserEntity> findUserByUserId(String userId);

    Completable saveUser(String userId);

    Single<String> saveBeacon(String userId, String beaconId);

    Observable<String> findBeaconsByUserId(String userId);

    Single<String> removeBeacon(String userId, String beaconId);

    Observable<UserEntity> observeUserProfile(String userId);
}
