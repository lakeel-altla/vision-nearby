package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllNearbyUserUseCase {

    @Inject
    BeaconRepository beaconsRepository;

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    FindAllNearbyUserUseCase() {
    }

    public Observable<UserProfile> execute(String beaconId) {
        return beaconsRepository.find(beaconId)
                .subscribeOn(Schedulers.io())
                .toObservable()
                // Exclude public beacon.
                .filter(beacon -> beacon != null)
                .flatMap(beacon -> findUser(beacon.userId));
    }

    private Observable<UserProfile> findUser(String userId) {
        return usersRepository.find(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}