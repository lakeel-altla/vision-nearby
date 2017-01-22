package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindUserIdByBeaconIdUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    FindUserIdByBeaconIdUseCase() {
    }

    public Single<BeaconEntity> execute(String beaconId) {
        return repository.findBeacon(beaconId).subscribeOn(Schedulers.io());
    }
}
