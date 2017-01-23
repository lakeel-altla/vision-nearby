package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindNearbyUsersUseCase {

    @Inject
    FirebaseBeaconsRepository beaconsRepository;

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    FindNearbyUsersUseCase() {
    }

    public Observable<UserEntity> execute(String beaconId) {
        return beaconsRepository.findBeacon(beaconId)
                .subscribeOn(Schedulers.io())
                .toObservable()
                // Exclude public beacon.
                .filter(entity -> entity != null)
                .flatMap(entity -> findUser(entity.userId));
    }

    private Observable<UserEntity> findUser(String userId) {
        return usersRepository.findUserByUserId(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}