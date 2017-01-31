package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;

import javax.inject.Inject;

import rx.Single;

public final class RemoveUserBeaconUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    RemoveUserBeaconUseCase() {
    }

    public Single<String> execute(String userId, String beaconId) {
        return repository.removeUserBeacon(userId, beaconId);
    }
}
