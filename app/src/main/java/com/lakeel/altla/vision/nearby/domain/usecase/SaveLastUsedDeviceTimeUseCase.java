package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveLastUsedDeviceTimeUseCase {

    @Inject
    FirebaseBeaconRepository repository;

    @Inject
    SaveLastUsedDeviceTimeUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.saveLastUsedDeviceTime(beaconId).subscribeOn(Schedulers.io());
    }
}
