package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    FindBeaconUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconId) {
        return repository.findBeacon(beaconId);
    }
}
