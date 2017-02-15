package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;

import javax.inject.Inject;

import rx.Single;

final class SaveUserBeaconUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    SaveUserBeaconUseCase() {
    }

    public Single<String> execute(String userId, String beaconId) {
        return repository.saveUserBeacon(userId, beaconId);
    }
}
