package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllDeviceUseCase {

    @Inject
    UserProfileRepository usersRepository;

    @Inject
    BeaconRepository beaconsRepository;

    @Inject
    FindAllDeviceUseCase() {
    }

    public Observable<Beacon> execute() {
        String userId = MyUser.getUserId();
        return usersRepository.findUserBeacons(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(this::findBeacon)
                .filter(entity -> entity != null);
    }

    private Observable<Beacon> findBeacon(String beaconId) {
        return beaconsRepository.find(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }
}
