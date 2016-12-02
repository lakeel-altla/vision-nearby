package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveTokensUseCase {

    @Inject
    FirebaseTokensRepository firebaseTokensRepository;

    @Inject
    SaveTokensUseCase() {
    }

    public Single<String> execute(String beaconId, String token) {
        return firebaseTokensRepository.saveTokenByBeaconId(beaconId, token);
    }
}
