package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindDeviceTokenUseCase {

    @Inject
    UserDeviceTokenRepository repository;

    @Inject
    FindDeviceTokenUseCase() {
    }
    public Single<String> execute(String userId) {
        return repository.find(userId);
    }
}
