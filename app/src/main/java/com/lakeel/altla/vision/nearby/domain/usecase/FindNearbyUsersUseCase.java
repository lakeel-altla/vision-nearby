package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.model.User;

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

    public Observable<User> execute(String beaconId) {
        return beaconsRepository.findBeacon(beaconId)
                .subscribeOn(Schedulers.io())
                .toObservable()
                // Exclude public beacon.
                .filter(beacon -> beacon != null)
                .flatMap(beacon -> findUser(beacon.userId));
    }

    private Observable<User> findUser(String userId) {
        return usersRepository.findUser(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}