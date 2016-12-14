package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Completable;

public final class FoundDeviceUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    FoundDeviceUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.foundDevice(beaconId);
    }
}
