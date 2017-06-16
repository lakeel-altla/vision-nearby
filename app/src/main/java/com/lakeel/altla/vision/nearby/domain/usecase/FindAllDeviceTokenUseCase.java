package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllDeviceTokenUseCase {

    @Inject
    UserDeviceTokenRepository repository;

    @Inject
    FindAllDeviceTokenUseCase() {
    }

    public Observable<DeviceToken> execute(@NonNull String userId) {
        return repository.findAll(userId).subscribeOn(Schedulers.io());
    }
}

