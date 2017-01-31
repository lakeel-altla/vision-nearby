package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveLastUsedTimeUseCase {

    @Inject
    FirebaseBeaconRepository repository;

    @Inject
    public SaveLastUsedTimeUseCase() {
    }

    public Completable execute(String beaconId) {
        return repository.saveLastUsedTime(beaconId).subscribeOn(Schedulers.io());
    }
}
