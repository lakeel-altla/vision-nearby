package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;

import javax.inject.Inject;

import rx.Single;

public final class RemoveUserBeaconUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    RemoveUserBeaconUseCase() {
    }

    public Single<String> execute(String userId, String beaconId) {
        return repository.removeUserBeacon(userId, beaconId);
    }
}
