package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.Single;

public final class RemoveBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    RemoveBeaconUseCase() {
    }

    public Single<String> execute(String beaconId) {
        return repository.removeBeaconByBeaconId(beaconId);
    }
}
