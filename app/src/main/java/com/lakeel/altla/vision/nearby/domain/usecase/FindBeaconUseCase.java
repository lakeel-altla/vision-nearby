package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindBeaconUseCase {

    @Inject
    BeaconRepository repository;

    @Inject
    FindBeaconUseCase() {
    }

    public Single<Beacon> execute(@NonNull String beaconId) {
        return repository.find(beaconId).subscribeOn(Schedulers.io());
    }
}
