package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Single;

public final class RemoveUserBeaconUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    RemoveUserBeaconUseCase() {
    }

    public Single<String> execute(String userId, String beaconId) {
        return repository.removeBeacon(userId, beaconId);
    }
}
