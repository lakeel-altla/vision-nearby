package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindDevicesUseCase {

    @Inject
    FirebaseUsersRepository usersRepository;

    @Inject
    FirebaseBeaconsRepository beaconsRepository;

    @Inject
    FindDevicesUseCase() {
    }

    public Observable<BeaconEntity> execute() {
        String userId = MyUser.getUserId();
        return usersRepository.findBeaconsByUserId(userId)
                .subscribeOn(Schedulers.io())
                .flatMap(this::findBeacon)
                .filter(entity -> entity != null);
    }

    private Observable<BeaconEntity> findBeacon(String beaconId) {
        return beaconsRepository.findBeacon(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }
}
