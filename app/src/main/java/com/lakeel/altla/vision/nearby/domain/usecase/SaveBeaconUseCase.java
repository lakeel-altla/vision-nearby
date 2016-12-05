package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    SaveBeaconUseCase() {
    }

    public Single<String> execute(String beaconId, String userId, String deviceName) {
        return repository.saveUserBeacon(beaconId, userId, deviceName);
    }
}
