package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class FoundDeviceUseCase {

    @Inject
    BeaconRepository repository;

    @Inject
    FoundDeviceUseCase() {
    }

    public Completable execute(@NonNull String beaconId) {
        return repository.foundDevice(beaconId).subscribeOn(Schedulers.io());
    }
}
