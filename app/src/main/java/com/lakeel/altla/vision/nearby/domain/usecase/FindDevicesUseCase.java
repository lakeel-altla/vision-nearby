package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindDevicesUseCase {

    @Inject
    FirebaseUserProfileRepository usersRepository;

    @Inject
    FirebaseBeaconRepository beaconsRepository;

    @Inject
    FindDevicesUseCase() {
    }

    public Observable<Beacon> execute() {
        String userId = MyUser.getUserId();
        return usersRepository.findUserBeacons(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(this::findBeacon)
                .filter(entity -> entity != null);
    }

    private Observable<Beacon> findBeacon(String beaconId) {
        return beaconsRepository.findBeacon(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }
}
