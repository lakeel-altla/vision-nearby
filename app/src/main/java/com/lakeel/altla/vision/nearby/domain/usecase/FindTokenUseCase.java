package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserDeviceTokenRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindTokenUseCase {

    @Inject
    FirebaseUserDeviceTokenRepository repository;

    @Inject
    FindTokenUseCase() {
    }
    public Single<String> execute(String userId) {
        return repository.findToken(userId);
    }
}
