package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserDeviceTokenRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindDeviceTokenUseCase {

    @Inject
    FirebaseUserDeviceTokenRepository repository;

    @Inject
    FindDeviceTokenUseCase() {
    }
    public Single<String> execute(String userId) {
        return repository.findDeviceToken(userId);
    }
}
