package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class FoundDeviceUseCase {

    @Inject
    FirebaseBeaconRepository repository;

    @Inject
    FoundDeviceUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.foundDevice(beaconId).subscribeOn(Schedulers.io());
    }
}
