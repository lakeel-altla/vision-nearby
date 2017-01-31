package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindDeviceTokensUseCase {

    @Inject
    FirebaseUserDeviceTokenRepository repository;

    @Inject
    FindDeviceTokensUseCase() {
    }

    public Observable<DeviceToken> execute(String userId) {
        return repository.findDeviceTokens(userId).subscribeOn(Schedulers.io());
    }
}

