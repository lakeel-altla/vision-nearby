package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindNearbyUserUseCase {

    @Inject
    BeaconRepository beaconsRepository;

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    FindNearbyUserUseCase() {
    }

    public Observable<UserProfile> execute(@NonNull String beaconId) {
        return beaconsRepository
                .find(beaconId)
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