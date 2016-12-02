package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;

import rx.Completable;
import rx.Observable;
import rx.Single;

public interface FirebaseUsersRepository {

    Single<UserEntity> findUserById(String userId);

    Single<UserEntity> findUserByName(String name);

    Completable saveUser();

    Single<String> saveBeacon(String userId, String beaconId);

    Observable<String> findBeaconsByUserId(String userId);
}
