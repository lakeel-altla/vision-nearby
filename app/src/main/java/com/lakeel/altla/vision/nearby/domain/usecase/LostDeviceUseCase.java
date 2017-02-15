package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class LostDeviceUseCase {

    @Inject
    BeaconRepository repository;

    @Inject
    LostDeviceUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.lostDevice(beaconId).subscribeOn(Schedulers.io());
    }
}
