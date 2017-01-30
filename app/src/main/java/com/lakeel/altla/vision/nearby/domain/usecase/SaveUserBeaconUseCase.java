package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveUserBeaconUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    SaveUserBeaconUseCase() {
    }

    public Single<String> execute(String userId, String beaconId) {
        return repository.saveBeacon(userId, beaconId);
    }
}
