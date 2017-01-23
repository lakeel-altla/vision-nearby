package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class LostDeviceUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    LostDeviceUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.lostDevice(beaconId).subscribeOn(Schedulers.io());
    }
}
